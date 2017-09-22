package tutorial;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

public class JxtaChessNetwork {
	
	private static final String JXTA_CHESS = "JxtaChess.";
	
	private static final int MSG_READ_TIMEOUT = 5 * 1000;
	
	private static final long REMOTE_DISCOVERY_INTERVAL = 30 * 1000;
	
	PeerGroup rootGroup;
	
	PipeService pipeService;
	
	DiscoveryService discoveryService;
	
	Hashtable creators;
	
	Hashtable games;
	
	Hashtable badGameAdvertisements;
	
	Hashtable openGameAdvertisements;
	
	Hashtable inProgressGameAdvertisements;
	
	Hashtable tournamentAdvertisements;
	
	Hashtable tournaments;
	
	Hashtable tournamentParticipants;
	
	Hashtable tournamentManagers;
	
	Hashtable tournamentGroups;
	
	GameDiscoverer discoverer;
	
	GameDiscoveryListener discoveryListener;
	
	MimeMediaType textXml = new MimeMediaType("text", "xml");
	
	public interface GameDiscoveryListener {
		
		public void discovered(String gameName, boolean isTournament,
			boolean inProgress);
		
		public void invalidated(String gameName, boolean isTournament,
			boolean inProgress);
		
	}
	
	public interface GameListener {
		
		public boolean wantToStart(String opponent);
		
		public void couldntStart(String opponent, GameException ex);
		
		public void start(Game game) throws GameException;
		
		public void startedObserving(Game game) throws GameException;
		
		public void move(char fromY, int fromX, char toY, int toX);
		
		public void opponentOffersDraw(String name);
		
		public void opponentAgreesToDraw(String name);
		
		public void opponentResigned(String name);
		
		public void addObserver(String name);
		
		public void removeObserver(String name);
		
		public void suggestion(String fromPlayer, char fromY, int fromX, char toY,
			int toX);
		
		public void badMessage();
		
		public void lostConnection();
		
		public void sameMove();
		
		public void setBoard(String map);
		
		public void setHistory(String moves);
		
		public void boardWanted();
		
		public void historyWanted();
		
		public void receivedChatMessage(String name, String message);
	}
	
	public static class GameException extends Exception {
		Exception e;
		
		GameException(String msg) {
			super(msg);
		}
		
		GameException(String msg, Exception e) {
			super(msg);
			this.e = e;
		}
		
		public Exception getNestedException() {
			return e;
		}
	}
	
	public class Game implements Runnable {
		String name;
		
		String whiteName;
		
		String blackName;
		
		String myName;
		
		PipeService pipeService;
		
		InputPipe in;
		
		OutputPipe out;
		
		GameListener listener;
		
		boolean done;
		
		int moveCount;
		
		String lastMove = null;
		
		String lastAck = null;
		
		int role;
		
		long myLastMoveTimestamp;
		
		long opponentLastMoveTimestamp;
		
		Game(String name, int role, String whiteName, String blackName,
			PipeService pipeService, InputPipe in, OutputPipe out,
			GameListener listener, String myName /* if observer */) {
			System.out.println("Game.myName = " + myName);
			this.name = name;
			this.role = role;
			this.whiteName = whiteName;
			this.blackName = blackName;
			this.pipeService = pipeService;
			this.in = in;
			this.out = out;
			this.listener = listener;
			this.myName = myName;
			this.done = false;
		}
		
		public String getName() {
			return name;
		}
		
		public String getWhiteName() {
			return whiteName;
		}
		
		public String getBlackName() {
			return blackName;
		}
		
		public void sendMessage(String value) throws IOException {
			Message msg = pipeService.createMessage();
			
			msg.setBytes("jxta-chess", value.getBytes());
			
			out.send(msg);
		}
		
		public void stopObserving() throws IOException {
			
			sendMessage("<jxta-chess>" + "  <stop-observing>" + "    <name>" + myName
				+ "</name>" + "  </stop-observing>" + "</jxta-chess>");
			
		}
		
		public void resign() throws IOException {
			sendMessage("<jxta-chess>" + "  <resignation>" + "    <name>"
				+ (role == Board.WHITE ? whiteName : blackName) + "    </name>"
				+ "    <text>thanks</text>" + "  </resignation>" + "</jxta-chess>");
		}
		
		public void offerDraw() throws IOException {
			
			sendMessage("<jxta-chess>" + "  <draw-offer>" + "    <name>"
				+ (role == Board.WHITE ? whiteName : blackName) + "    </name>"
				+ "    <text>thanks</text>" + "  </draw-offer>" + "</jxta-chess>");
		}
		
		public void sendChatMessage(String msg) throws IOException {
			System.out.println("myName = " + myName);
			sendMessage("<jxta-chess>" + "  <chat>" + "    <name>" + myName
				+ "</name>" + "    <message> " + msg + "</message>" + "  </chat>"
				+ "</jxta-chess>");
			
		}
		
		public void makeSuggestion(String name, char fromX, int fromY, char toX,
			int toY) throws IOException {
			sendMessage("<jxta-chess>" + "  <suggestion>" + "    <name>" + name
				+ "</name>" + "    <number>" + moveCount + "</number>" + "    <from>"
				+ fromX + "" + fromY + "</from>" + "    <to>" + toX + "" + toY
				+ "</to>" + "  </suggestion>" + "</jxta-chess>");
		}
		
		public void makeMove(char fromX, int fromY, char toX, int toY)
			throws IOException {
			
			++moveCount;
			myLastMoveTimestamp = System.currentTimeMillis();
			
			sendMessage("<jxta-chess>" + "  <move>" + "    <number>" + moveCount
				+ "</number>" + "    <from>" + fromX + "" + fromY + "</from>"
				+ "    <to>" + toX + "" + toY + "</to>" + "  </move>" + "</jxta-chess>");
			
		}
		
		public void sendHistory(String moves) throws IOException {
			System.out.println("Sending history.");
			sendMessage("<jxta-chess>" + "  <history>" + "    <moves>" + moves
				+ "</moves>" + "  </history>" + "</jxta-chess>");
			
		}
		
		public void sendBoard(String map) throws IOException {
			sendMessage("<jxta-chess>" + "  <board>" + "   <number>" + moveCount
				+ "</number>" + "   <map>" + map + "</map>" + "  </board>"
				+ "</jxta-chess>");
		}
		
		public void repeatLastMove() throws IOException {
			if (lastMove == null)
				return;
			debug("sending repeat of last move");
			
			sendMessage(lastMove);
			
		}
		
		public void ackLastMove() throws IOException {
			
			if (lastAck == null)
				lastAck =
					("<jxta-chess>" + "  <ack>" + "   <number>" + moveCount + "</number>"
						+ "  </ack>" + "</jxta-chess>");
			
			sendMessage(lastAck);
		}
		
		public void requestBoard() throws IOException {
			sendMessage("<jxta-chess>" + "  <board-wanted></board-wanted>"
				+ "</jxta-chess>");
			
		}
		
		public void requestHistory() throws IOException {
			sendMessage("<jxta-chess>" + "  <history-wanted></history-wanted>"
				+ "</jxta-chess>");
			
		}
		
		public void sendDrawAgreement() throws IOException {
			sendMessage("<jxta-chess>" + "  <draw-agreement>" + "    <name>"
				+ (role == Board.WHITE ? whiteName : blackName) + "   </name>"
				+ "  </draw-agreement>" + "</jxta-chess>");
		}
		
		public void run() {
			Message msg;
			
			try {
				int silent = 0;
				while (!isDone()) {
					
					try {
						msg = in.poll(MSG_READ_TIMEOUT);
						
						if (isDone())
							break;
						
						if (msg == null) {
							if (silent++ > 6) {
								silent = 0;
								repeatLastMove();
							}
							continue;
						}
						silent = 0;
						try {
							messageReceived(msg);
						} catch (IOException e) {
							listener.badMessage();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
							break;
						}
					} catch (InterruptedException e) {
						if (silent++ > 6) {
							silent = 0;
							repeatLastMove();
						}
					}
				}
				
			} catch (IOException e) {
				listener.lostConnection();
			}
			
		}
		
		void messageReceived(Message msg) throws IOException,
			InvocationTargetException {
			MessageElement el = msg.getElement("jxta-chess");
			
			if (el == null) {
				debugMsg("Received odd message", msg);
				return;
			}
			
			InputStream in = el.getStream();
			
			StructuredTextDocument doc =
				(StructuredTextDocument) StructuredDocumentFactory
					.newStructuredDocument(textXml, in);
			
			if (doc.getName().equals("board-wanted")) {
				debug("Received request for a full board.");
				listener.boardWanted();
				return;
			} else if (doc.getName().equals("history-wanted")) {
				listener.historyWanted();
				return;
			}
			
			debug("Game.messageReceived: " + doc.getName());
			
			boolean isAck = (doc.getName().equals("ack"));
			boolean isBoard = (doc.getName().equals("board"));
			boolean isSuggestion = (doc.getName().equals("suggestion"));
			Enumeration children = doc.getChildren();
			
			String number = null;
			String from = null;
			String to = null;
			String map = null;
			String name = null;
			
			if (doc.getName().equals("chat")) {
				String fromm = null;
				String message = null;
				
				while (children.hasMoreElements()) {
					TextElement tag = (TextElement) children.nextElement();
					if (tag.getName().equals("name"))
						fromm = tag.getTextValue();
					else if (tag.getName().equals("message"))
						message = tag.getTextValue();
				}
				
				if (fromm != null && message != null)
					listener.receivedChatMessage(fromm, message);
			} else if (doc.getName().equals("history")) {
				System.out.println("received history");
				while (children.hasMoreElements()) {
					TextElement tag = (TextElement) children.nextElement();
					if (tag.getName().equals("moves") && tag.getTextValue() != null)
						listener.setHistory(tag.getTextValue());
				}
				return;
			} else if (doc.getName().equals("observe")) {
				while (children.hasMoreElements()) {
					TextElement tag = (TextElement) children.nextElement();
					if (tag.getName().equals("name"))
						listener.addObserver(tag.getTextValue());
				}
				return;
			}
			if (doc.getName().equals("resignation")) {
				while (children.hasMoreElements()) {
					TextElement tag = (TextElement) children.nextElement();
					if (tag.getName().equals("name"))
						listener.opponentResigned(tag.getTextValue());
				}
				return;
			} else if (doc.getName().equals("draw-offer")) {
				while (children.hasMoreElements()) {
					TextElement tag = (TextElement) children.nextElement();
					if (tag.getName().equals("name"))
						listener.opponentOffersDraw(tag.getTextValue());
				}
				return;
			} else if (doc.getName().equals("stop-observing")) {
				while (children.hasMoreElements()) {
					TextElement tag = (TextElement) children.nextElement();
					if (tag.getName().equals("name")) {
						debug("name tag from stop-observer: " + tag.getTextValue());
						listener.removeObserver(tag.getTextValue());
					}
				}
				return;
			} else if (doc.getName().equals("draw-agreement")) {
				TextElement tag = (TextElement) children.nextElement();
				if (tag.getName().equals("name"))
					listener.opponentAgreesToDraw(tag.getTextValue());
			}
			
			while (children.hasMoreElements()) {
				TextElement tag = (TextElement) children.nextElement();
				
				if (tag.getName().equals("number"))
					number = tag.getTextValue();
				else if (tag.getName().equals("from"))
					from = tag.getTextValue();
				else if (tag.getName().equals("to"))
					to = tag.getTextValue();
				else if (tag.getName().equals("map"))
					map = tag.getTextValue();
				else if (tag.getName().equals("name"))
					name = tag.getTextValue();
			}
			
			if (number == null)
				return;
			
			int receivedCount = Integer.parseInt(number);
			
			if (isSuggestion) {
				
				debug("Received suggestion from " + from + " to " + to);
				listener.suggestion(name, from.charAt(0), from.charAt(1) - '0',
					to.charAt(0), to.charAt(1) - '0');
				return;
			}
			
			if (receivedCount < moveCount) {
				debug("Received old information.");
				return;
			}
			
			debug("move count = " + moveCount + ", received count = " + receivedCount);
			
			if (receivedCount == moveCount) { // ack business or ignore.
				if (isAck) {
					debug("Received ack of move number " + receivedCount);
					lastMove = null;
				} else {
					if (isBoard) {
						debug("redundant board; don't care");
						return; // redundant board; don't care.
					}
					
					// Redundant move. Therefore, we received it before as
					// a new move, therefore we have cleared our own last move,
					// Unless this is our own move, in which case we must not
					// ack it.
					// Also, listener shall not ack if it is an observer.
					if (lastMove == null) {
						debug("Received repeat of move number " + receivedCount);
						listener.sameMove();
					}
				}
				return;
			}
			
			if (isBoard) {
				// All maps showing a moveCount more recent than
				// ours are good to take.
				try {
					debug("Setting board to " + map);
					listener.setBoard(map);
					moveCount = receivedCount;
				} catch (Throwable t) {
					debug("Received broken board map.");
					t.printStackTrace();
					moveCount = 0;
				}
				return;
			}
			
			// Could be a hack of some move we missed, or could be a
			// move. If it's ahead of us either way, it's time to
			// ask for an update.
			if (receivedCount > moveCount + 1) {
				debug("Hey, we missed some moves here..." + "asking for an update.");
				requestBoard();
				return;
			}
			
			// Now it has to be a regular, in-sequence move or suggestion
			if (from == null || to == null || from.length() != 2
				|| from.length() != 2) {
				debug("Received broken move.");
				return;
			}
			
			debug("Received move from " + from + " to " + to);
			
			lastMove = null; // we can stop repeating our last move; we've just
			// received a valid countermove from the opponent.
			lastAck = null; // This is a new move; we'll need a new ack.
			
			listener.move(from.charAt(0), from.charAt(1) - '0', to.charAt(0),
				to.charAt(1) - '0');
			moveCount = receivedCount;
			
			// ackLastMove ();
		}
		
		synchronized boolean isDone() {
			return done;
		}
		
		synchronized void quit() {
			done = true;
		}
	}
	
	public JxtaChessNetwork(GameDiscoveryListener discoveryListener)
		throws IOException, PeerGroupException, Exception {
		debug("JxtaChessNetwork: joining the JXTA network");
		
		this.rootGroup = PeerGroupFactory.newNetPeerGroup();
		
		debug("JxtaChessNetwork: joined the JXTA network");
		
		this.discoveryListener = discoveryListener;
		this.creators = new Hashtable();
		this.games = new Hashtable();
		this.badGameAdvertisements = new Hashtable();
		this.openGameAdvertisements = new Hashtable();
		this.inProgressGameAdvertisements = new Hashtable();
		this.tournamentAdvertisements = new Hashtable();
		this.tournaments = new Hashtable();
		this.tournamentParticipants = new Hashtable();
		this.tournamentManagers = new Hashtable();
		this.tournamentGroups = new Hashtable();
		
		this.pipeService = rootGroup.getPipeService();
		this.discoveryService = rootGroup.getDiscoveryService();
		this.discoverer = new GameDiscoverer(discoveryService);
		
		new Thread(discoverer).start();
	}
	
	public String getPeerName() {
		return rootGroup.getPeerName();
	}
	
	public void stop() {
		discoverer.stop();
	}
	
	void debugMsg(String s, Message msg) {
		Enumeration e = msg.getElements();
		
		synchronized (System.out) {
			System.out.println(s);
			
			while (e.hasMoreElements()) {
				MessageElement mel = (MessageElement) e.nextElement();
				
				System.out.println("- " + mel.getName());
			}
		}
	}
	
	class TournamentManager implements PipeMsgListener {
		Tournament tournament;
		
		PeerGroup group;
		
		PipeAdvertisement wirePipeAdv;
		
		InputPipe inWirePipe;
		
		OutputPipe outWirePipe;
		
		boolean done;
		
		String namespace = "jxta-chess-tournament:";
		
		TournamentManager(Tournament tournament, PeerGroup group,
			PipeAdvertisement wirePipeAdv, InputPipe inWirePipe,
			OutputPipe outWirePipe) {
			this.tournament = tournament;
			this.group = group;
			this.wirePipeAdv = wirePipeAdv;
			this.inWirePipe = inWirePipe;
			this.outWirePipe = outWirePipe;
			this.done = false;
		}
		
		void start() {
			// tell all players tournament is starting
			
			try {
				broadcast("start", "let's go");
				
				// round info will be sent via a callback that will
				// dispatch the sendRoundInfo
				
			} catch (IOException e) {
				// do the dumb thing for now
				e.printStackTrace();
			}
		}
		
		void sendRoundInfo(Tournament.Round r) throws IOException {
			Enumeration e = r.getMatches();
			Tournament.Match m;
			
			broadcast("round-start", String.valueOf(r.getNumber()));
			
			while (e.hasMoreElements()) {
				m = (Tournament.Match) e.nextElement();
				
				try {
					broadcast("match", m.p1 + " vs. " + m.p2);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		void stop() {
			// tell all players tournament has ended
			try {
				broadcast("stop", "thanks");
				done = true;
			} catch (IOException e) {
				// we probably don't care anymore
			}
		}
		
		public void pipeMsgEvent(PipeMsgEvent ev) {
			Message msg = ev.getMessage();
			String value;
			
			debugMsg("TournamentManager: ", msg);
			
			try {
				if ((value = getValue(msg, "snapshot-request")) != null) {
					
					broadcast("snapshot", tournament.externalize());
					
				}
				if ((value = getValue(msg, "join")) != null) {
					
					try {
						tournament.addPlayer(value);
						debug("Added player " + value);
						broadcast("join-ack", value);
						
					} catch (IllegalArgumentException e) {
						broadcast("join-duplicate", value);
					}
					
				} else if ((value = getValue(msg, "leave")) != null) {
					
					tournament.playerLeft(value);
				} else if ((value = getValue(msg, "game-created")) != null) {
					tournament.listener.gameCreated(value);
				} else if ((value = getValue(msg, "game-started")) != null) {
					
					StringTokenizer st = new StringTokenizer(value, " ");
					String p1 = st.nextToken();
					st.nextToken();
					String p2 = st.nextToken();
					tournament.listener.gameStarted(p1, p2);
					
				} else if ((value = getValue(msg, "result")) != null) {
					System.out.println("Received result: " + value);
					StringTokenizer st = new StringTokenizer(value, " ,");
					String p1 = st.nextToken();
					st.nextToken();
					String p2 = st.nextToken();
					int result = Integer.parseInt(st.nextToken());
					
					tournament.getCurrentRound().setResult(p1, p2, result);
					
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		void broadcast(String tag, String value) throws IOException {
			Message msg = group.getPipeService().createMessage();
			
			msg.setBytes(namespace + tag, value.getBytes());
			
			outWirePipe.send(msg);
		}
		
		String getValue(Message msg, String tag) {
			return msg.getString(namespace + tag);
		}
	}
	
	public void joinTournament(String tournamentName, String myName,
		Tournament.Listener listener) throws GameException {
		
	}
	
	public void observeTournament(String tournamentName, String myName,
		Tournament.Listener listener) throws GameException {
		try {
			PeerGroup tournamentGroup = rootGroup;
			/*
			 * (PeerGroup) tournamentGroups.get (tournamentName); if (tournamentGroup
			 * == null) { PeerGroupAdvertisement adv = (PeerGroupAdvertisement)
			 * tournamentAdvertisements.get (tournamentName); debug ("Joining group "
			 * + adv.getID ()); tournamentGroup = rootGroup.newGroup (adv);
			 * tournamentGroups.put (tournamentName, tournamentGroup); } debug
			 * ("Joined group " + tournamentGroup.getPeerGroupID ());
			 */
			
			DiscoveryService discoveryService = tournamentGroup.getDiscoveryService();
			
			TournamentManagerFinder finder =
				new TournamentManagerFinder(tournamentGroup, tournamentName, myName,
					listener);
			
			PipeAdvertisement wireAdv =
				(PipeAdvertisement) tournamentAdvertisements.get(tournamentName);
			finder.tryToConnect(wireAdv);
			
			/*
			 * debug ("Going to get local advs."); Enumeration en =
			 * discoveryService.getLocalAdvertisements (DiscoveryService.ADV, "Name",
			 * JXTA_CHESS + "Tournament." + tournamentName); debug ("Got local advs: "
			 * + en.hasMoreElements ()); while (en.hasMoreElements ()) {
			 * PipeAdvertisement adv = (PipeAdvertisement) en.nextElement (); debug
			 * ("Going to try to connect to " + adv.getName ()); try {
			 * finder.tryToConnect ((PipeAdvertisement) adv); return; } catch
			 * (IOException e) { listener.networkIsDown (e); } }
			 */
			
			// new Thread (finder).start ();
			
			/*
			 * } catch (PeerGroupException e) { throw new GameException
			 * ("Could not join group " + tournamentName, e);
			 */
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void createTournament(String name, int gameTimeLimit, int totalRounds,
		Tournament.Listener listener) throws GameException, IOException {
		PeerGroup group;
		Tournament tournament;
		
		if (tournamentAdvertisements.get(name) == null) {
			
			tournament =
				new Tournament(name, gameTimeLimit, totalRounds, listener, true);
			
			tournaments.put(name, tournament);
			
			try {
				group = rootGroup;
				/*
				 * group = createChildGroup (rootGroup, null, JXTA_CHESS + "Tournament."
				 * + name, "JXTA Chess tournament " + name); debug ("Created group " +
				 * group.getPeerGroupID ()); tournamentGroups.put (name, group);
				 * tournamentAdvertisements.put (name, group.getPeerGroupAdvertisement
				 * ()); } catch (PeerGroupException e) { throw new GameException
				 * ("Could not create peer group.", e);
				 */
			} catch (Exception e) {
				// this comes from getAllPurpose stuff, we don't know what
				// it wanted to tell us.
				throw new GameException("Garbage-in, garbage-out.", e);
			}
			
			PipeAdvertisement wirePipeAdv =
				wirePipeAdv =
					(PipeAdvertisement) AdvertisementFactory
						.newAdvertisement("jxta:PipeAdvertisement");
			
			wirePipeAdv.setPipeID(IDFactory.newPipeID(
				(PeerGroupID) rootGroup.getPeerGroupID(), "foo".getBytes()));
			wirePipeAdv.setName(JXTA_CHESS + "Tournament." + name);
			
			wirePipeAdv.setType(PipeService.PropagateType);
			
			group.getDiscoveryService().remotePublish(wirePipeAdv,
				DiscoveryService.ADV);
			
			group.getDiscoveryService().publish(wirePipeAdv, DiscoveryService.ADV);
			
			tournamentAdvertisements.put(name, wirePipeAdv);
			
			System.out.println("Published wire adv for tournament.");
			
			OutputPipe outWirePipe =
				group.getPipeService().createOutputPipe(wirePipeAdv,
					10 * MSG_READ_TIMEOUT);
			
			TournamentManager manager =
				new TournamentManager(tournament, group, wirePipeAdv, null, outWirePipe);
			
			manager.inWirePipe =
				group.getPipeService().createInputPipe(wirePipeAdv, manager);
			
			tournamentManagers.put(name, manager);
			
			tournament.listener.created(tournament, manager);
			
			discoveryListener.discovered(name, true, false);
		} else {
			throw new GameException("There is already a tournament under "
				+ "that name.");
		}
	}
	
	public void createGame(TournamentParticipant tp, String name,
		GameListener listener) throws GameException {
		DiscoveryService discoveryService;
		BidirectionalPipeService bidirPipeService;
		PeerGroup group;
		String pipeName;
		
		if (tp == null)
			group = rootGroup;
		else
			group = tp.group;
		
		discoveryService = group.getDiscoveryService();
		bidirPipeService = new BidirectionalPipeService(group);
		
		if (creators.get(name) == null) {
			
			if (tp == null) {
				pipeName =
					"Game." + name + "." + String.valueOf(System.currentTimeMillis());
			} else {
				pipeName =
					tp.tournament.getName() + "." + name + "."
						+ String.valueOf(System.currentTimeMillis());
				;
			}
			
			try {
				BidirectionalPipeService.AcceptPipe acceptPipe =
					bidirPipeService.bind(JXTA_CHESS + pipeName);
				
				openGameAdvertisements.put(name, acceptPipe.getAdvertisement());
				discoveryListener.discovered(name, false, false);
				GameManager creator =
					new GameManager(tp, name, group, acceptPipe, listener);
				
				new Thread(creator).start();
				
				if (tp != null) {
					tp.sendGameCreated(name, acceptPipe.getAdvertisement());
				}
				
			} catch (IOException e) {
				throw new GameException("Could not create bidirectional "
					+ "accepting pipe.", e);
			}
			
		} else {
			throw new GameException("You already have a game started "
				+ "under that name.");
		}
	}
	
	class GameManager implements Runnable,
		BidirectionalPipeService.MessageListener {
		TournamentParticipant tp;
		
		String name;
		
		PeerGroup group;
		
		BidirectionalPipeService.AcceptPipe acceptPipe;
		
		GameListener listener;
		
		boolean isAccepting;
		
		Game game;
		
		Hashtable gcrTable;
		
		GameManager(TournamentParticipant tp, String name, PeerGroup group,
			BidirectionalPipeService.AcceptPipe acceptPipe, GameListener listener) {
			this.tp = tp;
			this.name = name;
			this.group = group;
			this.acceptPipe = acceptPipe;
			this.listener = listener;
			this.isAccepting = true;
			this.gcrTable = new Hashtable();
		}
		
		public void run() {
			try {
				while (isAccepting()) {
					
					try {
						BidirectionalPipeService.Pipe pipe =
							acceptPipe.accept(10 * 1000, this);
						
						debug("Accepted connection, ip = " + pipe.getInputPipe());
					} catch (InterruptedException e) {
						// timeout expired
					}
				}
			} catch (IOException e) {
				// isAccepting = false;
				e.printStackTrace();
			}
		}
		
		public void messageReceived(Message msg, OutputPipe pipe) {
			String val;
			
			if ((val = msg.getString("jxta-chess:join-game")) != null) {
				
				if (game == null) {
					
					// work around a bug in PropagateType pipes
					// that duplicate the first message sent (?!)
					
					GameCreationRunner gcr = (GameCreationRunner) gcrTable.get(val);
					
					if (gcr == null) {
						gcr =
							new GameCreationRunner(name, val, group, acceptPipe, pipe,
								listener, this);
						gcrTable.put(val, val);
						new Thread(gcr).start();
					}
				}
			}
			
		}
		
		synchronized boolean isAccepting() {
			return isAccepting;
		}
		
		synchronized void startedGame(Game g) {
			game = g;
			
			if (tp != null) {
				try {
					tp.sendGameStarted(g.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class GameCreationRunner implements Runnable {
		
		String name;
		
		PeerGroup group;
		
		BidirectionalPipeService.AcceptPipe acceptPipe;
		
		OutputPipe pipe;
		
		GameListener listener;
		
		GameManager gameManager;
		
		String opponentName;
		
		String gameName;
		
		GameCreationRunner(String name, String opponentName, PeerGroup group,
			BidirectionalPipeService.AcceptPipe acceptPipe, OutputPipe pipe,
			GameListener listener, GameManager gameManager) {
			this.name = name;
			this.opponentName = opponentName;
			this.group = group;
			this.acceptPipe = acceptPipe;
			this.pipe = pipe;
			this.listener = listener;
			this.gameManager = gameManager;
			this.gameName = name + " vs. " + opponentName;
		}
		
		public void run() {
			
			try {
				process();
			} catch (GameException e) {
				listener.couldntStart(opponentName, e);
			}
		}
		
		void process() throws GameException {
			
			PipeAdvertisement wirePipeAdv;
			Message msg;
			InputPipe inWirePipe;
			OutputPipe outWirePipe;
			
			// start automatically if in tournament mode
			// of, if just playing, ask the user if he wants to
			// play with this guy
			if (gameManager.tp != null || listener.wantToStart(opponentName)) {
				
				PipeService pipeService = group.getPipeService();
				
				try {
					msg = pipeService.createMessage();
					
					wirePipeAdv =
						(PipeAdvertisement) AdvertisementFactory
							.newAdvertisement("jxta:PipeAdvertisement");
					
					wirePipeAdv.setPipeID(IDFactory.newPipeID((PeerGroupID) group
						.getPeerGroupID()));
					wirePipeAdv.setName(JXTA_CHESS + gameName);
					
					wirePipeAdv.setType(PipeService.PropagateType);
					
					inWirePipe = pipeService.createInputPipe(wirePipeAdv);
					outWirePipe =
						pipeService.createOutputPipe(wirePipeAdv, 10 * MSG_READ_TIMEOUT);
					
					discoveryService.publish(wirePipeAdv, DiscoveryService.ADV);
					discoveryService.remotePublish(wirePipeAdv, DiscoveryService.ADV);
					
					inProgressGameAdvertisements.put(gameName, wirePipeAdv);
					
					discoveryListener.discovered(gameName, false, true);
					discoveryListener.invalidated(name, false, false);
					
					msg.addElement(msg.newMessageElement("jxta-chess:wire-pipe-adv",
						textXml, wirePipeAdv.getDocument(textXml).getStream()));
					
					msg.setString("jxta-chess:join-game", "accepted");
					
					pipe.send(msg);
					
				} catch (IOException e) {
					throw new GameException("Could not send message with"
						+ " wire advertisement back to" + " " + opponentName);
				}
				
				debug("Starting game " + gameName);
				
				Game game =
					new Game(gameName, Board.WHITE, name, opponentName,
						group.getPipeService(), inWirePipe, outWirePipe, listener, name);
				
				games.put(gameName, game);
				
				gameManager.startedGame(game);
				
				// may throw GameException
				listener.start(game);
				
				new Thread(game).start();
				
			}
		}
	}
	
	public void joinGame(TournamentParticipant tp, String opponent, String me,
		GameListener listener) throws GameException {
		// connect to pipe opened by the given waiting opponent
		// give it the pipe adv for where the opponent should
		// send the wire advertisement after he creates the wire
		// read the wire adv and start reading msgs from the wire
		
		PeerGroup group = (tp == null ? rootGroup : tp.group);
		
		new Thread(new GameJoiner(group, opponent, me, listener)).start();
	}
	
	class GameJoiner implements Runnable {
		
		PeerGroup group;
		
		String opponent;
		
		String me;
		
		GameListener listener;
		
		BidirectionalPipeService bidirPipeService;
		
		PipeService pipeService;
		
		GameJoiner(PeerGroup group, String opponent, String me,
			GameListener listener) {
			this.group = group;
			this.opponent = opponent;
			this.me = me;
			this.listener = listener;
			this.bidirPipeService = new BidirectionalPipeService(group);
			this.pipeService = group.getPipeService();
		}
		
		public void run() {
			try {
				sendJoinMessage();
			} catch (GameException e) {
				listener.couldntStart(opponent, e);
			}
		}
		
		public void sendJoinMessage() throws GameException {
			PipeAdvertisement adv;
			Message msg;
			BidirectionalPipeService.Pipe pipe;
			
			adv = (PipeAdvertisement) openGameAdvertisements.get(opponent);
			
			try {
				
				pipe = bidirPipeService.connect(adv, MSG_READ_TIMEOUT);
				// PipeService.PropagateType);
				
			} catch (IOException e) {
				
				discoveryListener.invalidated(opponent, false, false);
				
				openGameAdvertisements.remove(opponent);
				
				debug("Placing " + adv.getName() + " into bad table.");
				badGameAdvertisements.put(adv.getName(), adv.getName());
				
				throw new GameException("Unable to resolve pipe that was "
					+ "advertising the open game.  The "
					+ "game has probably been already " + "started.", e);
				
			}
			
			try {
				debug("Sending join request for " + me);
				
				msg = pipeService.createMessage();
				
				msg.setString("jxta-chess:join-game", me);
				
				pipe.getOutputPipe().send(msg);
				
				Game game =
					waitForJoinResponse(pipe.getInputPipe(), opponent + " vs. " + me);
				
				games.put(game.getName(), game);
				
				listener.start(game);
				
				new Thread(game).start();
				
			} catch (IOException e) {
				
				discoveryListener.invalidated(opponent, false, false);
				openGameAdvertisements.remove(opponent);
				
				throw new GameException("Could not communicate with " + "opponent.", e);
			}
			
		}
		
		Game waitForJoinResponse(InputPipe in, String gameName) throws IOException {
			Message msg;
			String val;
			PipeAdvertisement wirePipeAdv;
			OutputPipe out;
			
			try {
				debug("Thread " + Thread.currentThread() + "going into poll.");
				msg = in.poll(90 * 1000);
				
				if (msg != null) {
					
					if (((val = msg.getString("jxta-chess:join-game")) != null) &&
					
					val.equals("accepted")) {
						
						wirePipeAdv =
							(PipeAdvertisement) AdvertisementFactory.newAdvertisement(
								new MimeMediaType("text/xml"),
								msg.getElement("jxta-chess:wire-pipe-adv").getStream());
						
						in = pipeService.createInputPipe(wirePipeAdv);
						out =
							pipeService.createOutputPipe(wirePipeAdv, 10 * MSG_READ_TIMEOUT);
						return new Game(gameName, Board.BLACK, opponent, me, pipeService,
							in, out, listener, me);
						
					}
				}
			} catch (InterruptedException e) {
				// no message arrived before timeout expired
				// } catch (IOException e) {
				// could not read advertisement
			}
			
			throw new IOException("Opponent " + opponent
				+ " failed to respond to your request "
				+ " to play with him or her in 90 " + " seconds.");
		}
	}
	
	public void observeGame(TournamentParticipant tp, String name, String me,
		GameListener listener) throws GameException {
		PeerGroup group;
		Game game;
		PipeAdvertisement wireAdv;
		PipeService pipeService;
		InputPipe in;
		OutputPipe out;
		Message msg;
		
		wireAdv = (PipeAdvertisement) inProgressGameAdvertisements.get(name);
		
		if (wireAdv == null)
			throw new GameException("No such game in progress.");
		else {
			if (tp == null)
				group = rootGroup;
			else
				group = tp.group;
			
			pipeService = group.getPipeService();
			
			// ugh, one of the first things to fix:
			StringTokenizer st = new StringTokenizer(name, " ");
			String p1 = st.nextToken();
			st.nextToken();
			String p2 = st.nextToken();
			
			try {
				
				in = pipeService.createInputPipe(wireAdv);
				out = pipeService.createOutputPipe(wireAdv, 10 * MSG_READ_TIMEOUT);
				
				msg = pipeService.createMessage();
				
				msg.setString("jxta-chess", "<jxta-chess>" + "  <observe><name>" + me
					+ "</name></observe>" + "</jxta-chess>");
				
				out.send(msg);
				
				game =
					new Game(name, Board.OBSERVER, p1, p2, pipeService, in, out,
						listener, me);
				
				games.put(game.getName(), game);
				
				listener.start(game);
				
				new Thread(game).start();
				
			} catch (IOException e) {
				throw new GameException("Unable to publish game " + "advertisement.", e);
			}
		}
		
		/*
		 * } else { // maybe the user is playing with himself? listener.start
		 * (game); }
		 */
	}
	
	public void quitGame(String name) {
		Game g = (Game) games.get(name);
		
		// stop accepting messages
		g.quit();
		
		// leave the group
		
	}
	
	class GameDiscoverer implements Runnable {
		
		DiscoveryService discoveryService;
		
		boolean isRunning;
		
		GameDiscoverer(DiscoveryService discoveryService) {
			
			this.discoveryService = discoveryService;
			this.isRunning = true;
		}
		
		synchronized boolean isRunning() {
			return isRunning;
		}
		
		synchronized void stop() {
			isRunning = false;
		}
		
		public void run() {
			
			while (isRunning()) {
				
				discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV,
					"Name", JXTA_CHESS + "Game.*", 100, new AllGameDiscoveryListener());
				
				discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV,
					"Name", JXTA_CHESS + "Tournament.*", 100,
					new TournamentDiscoveryListener());
				try {
					Thread.sleep(REMOTE_DISCOVERY_INTERVAL);
				} catch (InterruptedException ex) {
					stop();
				}
			}
		}
	}
	
	/*
	 * private PipeAdvertisement createPipeAdv (String name) throws
	 * InvocationTargetException { PipeAdvertisement gamePipeAdv =
	 * (PipeAdvertisement) AdvertisementFactory.newAdvertisement
	 * ("jxta:PipeAdvertisement"); gamePipeAdv.setPipeID (IDFactory.newPipeID
	 * ((PeerGroupID)peerGroup.getPeerGroupID ())); gamePipeAdv.setName
	 * (JXTA_CHESS + name); gamePipeAdv.setType (PipeService.PropagateType);
	 * return gamePipeAdv; }
	 */
	
	private PeerGroup createChildGroup(PeerGroup parent, PeerGroupID id,
		String name, String description) throws PeerGroupException, IOException,
		Exception {
		ModuleImplAdvertisement implAdv =
			parent.getAllPurposePeerGroupImplAdvertisement();
		
		PeerGroup child = parent.newGroup(id, implAdv, name, description);
		System.out.println("Is this peer a rendezvous? " + child.isRendezvous());
		child.getRendezVousService().startRendezVous();
		System.out.println("Is this peer a rendezvous? " + child.isRendezvous());
		PeerGroupAdvertisement adv = child.getPeerGroupAdvertisement();
		
		DiscoveryService discoveryService = parent.getDiscoveryService();
		
		discoveryService.remotePublish(adv, DiscoveryService.GROUP);
		discoveryService.publish(adv, DiscoveryService.GROUP);
		
		child.init(parent, child.getPeerGroupID(), implAdv);
		
		return child;
	}
	
	private boolean isTournament(PipeAdvertisement adv) {
		return adv.getName().indexOf("Tournament") != -1;
	}
	
	private boolean isGame(PipeAdvertisement adv) {
		return adv.getName().indexOf("Game") != -1;
	}
	
	private String getName(Advertisement adv) {
		String name;
		
		if (adv instanceof PipeAdvertisement)
			name = ((PipeAdvertisement) adv).getName();
		else
			name = ((PeerGroupAdvertisement) adv).getName();
		
		if (name.startsWith(JXTA_CHESS))
			
			name = name.substring(JXTA_CHESS.length());
		
		if (name.startsWith("Tournament."))
			name = name.substring("Tournament.".length());
		else if (name.startsWith("Game."))
			name = name.substring("Game.".length());
		
		int nextDot = name.indexOf('.');
		
		if (nextDot != -1)
			name = name.substring(0, nextDot);
		
		return name;
	}
	
	class TournamentParticipant implements Runnable {
		
		PeerGroup group;
		
		InputPipe inWirePipe;
		
		OutputPipe outWirePipe;
		
		String myName;
		
		PipeService pipeService;
		
		Tournament tournament;
		
		boolean done;
		
		boolean needSnapshot;
		
		String namespace = "jxta-chess-tournament:";
		
		TournamentParticipant(PeerGroup group, InputPipe inWirePipe,
			OutputPipe outWirePipe, String myName, Tournament tournament) {
			this.group = group;
			this.inWirePipe = inWirePipe;
			this.outWirePipe = outWirePipe;
			this.myName = myName;
			this.tournament = tournament;
			this.pipeService = group.getPipeService();
			
			this.done = false;
			this.needSnapshot = true;
		}
		
		public void run() {
			Message m;
			String v;
			
			debug("TournamentParticipant: run() start");
			
			try {
				while (!done) {
					m = inWirePipe.poll(MSG_READ_TIMEOUT);
					
					if (m != null) {
						
						debugMsg("TournamentParticipant", m);
						
						if ((v = m.getString(namespace + "join-ack")) != null) {
							debug("join-ack = " + v);
							debug("myName = " + myName);
							
							tournament.addPlayer(v);
							if (v.equals(myName))
								tournament.listener.joined(tournament, this);
							
						} else if ((v = m.getString(namespace + "join-duplicate")) != null) {
							if (v.equals(myName))
								tournament.listener.needNameChange();
						} else if ((v = m.getString(namespace + "leave")) != null) {
							tournament.playerLeft(v);
						} else if ((v = m.getString(namespace + "snapshot")) != null) {
							if (needSnapshot) {
								tournament.initialize(v);
								needSnapshot = false;
								tournament.listener.observing(tournament, this);
							}
						} else if ((v = m.getString(namespace + "start")) != null) {
							tournament.startTournament();
						} else if ((v = m.getString(namespace + "end")) != null) {
							tournament.endTournament();
						} else if ((v = m.getString(namespace + "round-start")) != null) {
							tournament.startNextRound();
						} else if ((v = m.getString(namespace + "match")) != null) {
							StringTokenizer st = new StringTokenizer(v, " ");
							String p1 = st.nextToken();
							st.nextToken();
							String p2 = st.nextToken();
							tournament.getCurrentRound().setMatch(p1, p2);
						} else if ((v = m.getString(namespace + "game-created")) != null) {
							try {
								PipeAdvertisement wirePipeAdv =
									(PipeAdvertisement) AdvertisementFactory.newAdvertisement(
										new MimeMediaType("text/xml"),
										m.getElement(namespace + "game-wire-adv").getStream());
								openGameAdvertisements.put(v, wirePipeAdv);
								tournament.listener.gameCreated(v);
								// joinGame (this, v, myName, );
							} catch (IOException e) {
								// could not read advertisement
								e.printStackTrace();
							}
						} else if ((v = m.getString(namespace + "game-started")) != null) {
							
							/*
							 * StringTokenizer st = new StringTokenizer (v, " "); String p1 =
							 * st.nextToken (); st.nextToken (); String p2 = st.nextToken ();
							 * tournament.listener.gameCreated (p1, p2);
							 */
							
						} else if ((v = m.getString(namespace + "result")) != null) {
							StringTokenizer st = new StringTokenizer(v, " ,");
							String p1 = st.nextToken();
							st.nextToken();
							String p2 = st.nextToken();
							int result = Integer.parseInt(st.nextToken());
							
							tournament.getCurrentRound().setResult(p1, p2, result);
						}
					}
				}
			} catch (InterruptedException e) {
				done = true;
			}
			
			debug("TournamentParticipant: run() end");
		}
		
		void sendGameStarted(String name) throws IOException {
			sendMessage("game-started", name);
		}
		
		void sendGameCreated(String name, PipeAdvertisement gameWireAdv)
			throws IOException {
			Message m = pipeService.createMessage();
			
			m.setString(namespace + "game-created", name);
			
			m.addElement(m.newMessageElement(namespace + "game-wire-adv", textXml,
				gameWireAdv.getDocument(textXml).getStream()));
			
			outWirePipe.send(m);
		}
		
		public void sendMessage(String tag, String value) throws IOException {
			Message m = pipeService.createMessage();
			
			m.setString(namespace + tag, value);
			
			outWirePipe.send(m);
		}
		
		public void requestSnapshot() throws IOException {
			sendMessage("snapshot-request", "thanks");
		}
		
		public void join(String name) throws IOException {
			
			myName = name;
			
			sendMessage("join", myName);
			
		}
		
		public void leave() throws IOException {
			
			sendMessage("leave", myName);
			
		}
		
		public void sendResult(Game game, int result) throws IOException {
			
			System.out.println("Sending ersult " + result);
			
			sendMessage("result", game.getName() + " " + result);
			
		}
		
		public void stopObserving() {
			done = true;
			outWirePipe.close();
			
		}
	}
	
	class TournamentManagerFinder implements Runnable, DiscoveryListener {
		PeerGroup group;
		
		String myName;
		
		String tournamentName;
		
		Tournament.Listener listener;
		
		boolean done;
		
		TournamentManagerFinder(PeerGroup group, String tournamentName,
			String myName, Tournament.Listener listener) {
			this.group = group;
			this.tournamentName = tournamentName;
			this.myName = myName;
			this.listener = listener;
		}
		
		// this crap fails... just create the wire adv with the same
		// name and be done with it?
		
		public void run() {
			for (int i = 0; !done && i < 5; i++) {
				System.out.println("Attempt #" + (i + 1) + " to locate tournament "
					+ tournamentName);
				
				group.getDiscoveryService().getRemoteAdvertisements(null,
					DiscoveryService.PEER, "Name", "*",
					
					// JXTA_CHESS+
					// "Tournament.*", //+
					// tournamentName,
					100, this);
				
				group.getDiscoveryService().getRemoteAdvertisements(null,
					DiscoveryService.ADV, "Name", "*",
					
					// JXTA_CHESS+
					// "Tournament.*", //+
					// tournamentName,
					100, this);
				try {
					Thread.currentThread().sleep(REMOTE_DISCOVERY_INTERVAL);
				} catch (InterruptedException e) {
					done = true;
				}
			}
		}
		
		public void discoveryEvent(DiscoveryEvent event) {
			debug("TournamentManagerFinder.discoveryEvent");
			
			DiscoveryResponseMsg msg = event.getResponse();
			Enumeration advs = msg.getResponses();
			
			while (advs.hasMoreElements()) {
				String advStr = (String) advs.nextElement();
				
				try {
					PipeAdvertisement managerAdv =
						(PipeAdvertisement) AdvertisementFactory.newAdvertisement(textXml,
							new ByteArrayInputStream(advStr.getBytes()));
					
					tryToConnect(managerAdv);
					
					break;
				} catch (IOException e) {
					// was reading from a string, so this exception is from
					// tryToConnect
					listener.networkIsDown(e);
				}
			}
		}
		
		void tryToConnect(PipeAdvertisement wireAdv) throws IOException {
			PipeService pipeService = group.getPipeService();
			Tournament t = new Tournament(tournamentName, 0, /* don't know */
			0, /* these values */
			/* yet */
			listener, false);
			InputPipe wireInputPipe = pipeService.createInputPipe(wireAdv);
			OutputPipe wireOutputPipe =
				pipeService.createOutputPipe(wireAdv, 10 * MSG_READ_TIMEOUT);
			
			TournamentParticipant tp =
				new TournamentParticipant(group, wireInputPipe, wireOutputPipe, myName,
					t);
			
			tournaments.put(tournamentName, t);
			
			tournamentParticipants.put(tournamentName, tp);
			
			done = true;
			
			new Thread(tp).start();
			
			tp.requestSnapshot();
		}
	}
	
	class AllGameDiscoveryListener implements DiscoveryListener {
		
		public void discoveryEvent(DiscoveryEvent event) {
			DiscoveryResponseMsg msg = event.getResponse();
			Enumeration advs = msg.getResponses();
			
			while (advs.hasMoreElements()) {
				String advStr = (String) advs.nextElement();
				
				try {
					PipeAdvertisement adv =
						(PipeAdvertisement) AdvertisementFactory.newAdvertisement(textXml,
							new ByteArrayInputStream(advStr.getBytes()));
					System.out.println("Found game: " + adv.getName());
					
					if (isGame(adv) && badGameAdvertisements.get(adv.getName()) == null) {
						
						String name = getName(adv);
						
						if (name.indexOf(" vs. ") == -1) {
							
							if (openGameAdvertisements.get(name) == null) {
								
								openGameAdvertisements.put(name, adv);
								
								JxtaChessNetwork.this.discoveryListener.discovered(name, false,
									false);
							}
						} else {
							if (inProgressGameAdvertisements.get(name) == null) {
								
								inProgressGameAdvertisements.put(name, adv);
								
								JxtaChessNetwork.this.discoveryListener.discovered(name, false,
									true);
							}
						}
					}
					
				} catch (IOException e) {
					// was reading from a string
				}
			}
		}
	}
	
	class TournamentDiscoveryListener implements DiscoveryListener {
		
		public void discoveryEvent(DiscoveryEvent event) {
			
			DiscoveryResponseMsg msg = event.getResponse();
			
			Enumeration advs = msg.getResponses();
			
			while (advs.hasMoreElements()) {
				String advStr = (String) advs.nextElement();
				
				try {
					PipeAdvertisement adv =
						(PipeAdvertisement) AdvertisementFactory.newAdvertisement(textXml,
							new ByteArrayInputStream(advStr.getBytes()));
					
					debug("Looking at " + adv.getName());
					
					if (badGameAdvertisements.get(adv.getName()) == null) {
						String name = getName(adv);
						
						if (// (adv instanceof PipeAdvertisement) &&
						(isTournament(adv)) && (tournamentAdvertisements.get(name) == null))
						
						/*
						 * if ((adv instanceof PeerGroupAdvertisement) && (isTournament
						 * (name)) && (tournamentAdvertisements.get(name) == null))
						 */
						{
							
							tournamentAdvertisements.put(name, adv);
							JxtaChessNetwork.this.discoveryListener.discovered(name, true,
								true);
						}
					}
					
				} catch (IOException e) {
					// was reading from a string
				}
				
			}
		}
	}
	
	void debug(String s) {
		System.out.println(s);
	}
	
}