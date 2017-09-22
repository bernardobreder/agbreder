/*
 * Copyright (c) 2001-2007 Sun Microsystems, Inc. All rights reserved. The Sun
 * Project JXTA(TM) Software License Redistribution and use in source and binary
 * forms, with or without modification, are permitted provided that the
 * following conditions are met: 1. Redistributions of source code must retain
 * the above copyright notice, this list of conditions and the following
 * disclaimer. 2. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution. 3. The
 * end-user documentation included with the redistribution, if any, must include
 * the following acknowledgment: "This product includes software developed by
 * Sun Microsystems, Inc. for JXTA(TM) technology." Alternately, this
 * acknowledgment may appear in the software itself, if and wherever such
 * third-party acknowledgments normally appear. 4. The names "Sun",
 * "Sun Microsystems, Inc.", "JXTA" and "Project JXTA" must not be used to
 * endorse or promote products derived from this software without prior written
 * permission. For written permission, please contact Project JXTA at
 * http://www.jxta.org. 5. Products derived from this software may not be called
 * "JXTA", nor may "JXTA" appear in their name, without prior written permission
 * of Sun. THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL SUN MICROSYSTEMS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. JXTA is a
 * registered trademark of Sun Microsystems, Inc. in the United States and other
 * countries. Please see the license information page at :
 * <http://www.jxta.org/project/www/license.html> for instructions on use of the
 * license in source files.
 * ==================================================================== This
 * software consists of voluntary contributions made by many individuals on
 * behalf of Project JXTA. For more information on Project JXTA, please see
 * http://www.jxta.org. This license is based on the BSD license adopted by the
 * Apache Foundation.
 */
package tutorial.service;

import java.io.File;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredTextDocument;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.ModuleClassID;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

/**
 * ServiceServer side: This is the server side of the JXTA-EX1 example. The
 * server side application advertises the JXTA-EX1 service, starts the service,
 * and receives messages on a service defined pipe endpoint. The service
 * associated module spec and class advertisement are published in the
 * NetPeerGroup. Clients can discover the module advertisements and create
 * output pipeService to connect to the service. The server application creates
 * an input pipe that waits to receive messages. Each message received is
 * printed to the screen. We run the server as a daemon in an infinite loop,
 * waiting to receive client messages.
 */
public class ServiceServer {
	
	static PeerGroup netPeerGroup = null;
	
	static PeerGroupAdvertisement groupAdvertisement = null;
	
	private DiscoveryService discovery;
	
	private PipeService pipeService;
	
	private InputPipe serviceInputPipe;
	
	private NetworkManager manager;
	
	/**
	 * A pre-baked PipeID string
	 */
	public final static String PIPEIDSTR =
		"urn:jxta:uuid-9CCCDF5AD8154D3D87A391210404E59BE4B888209A2241A4A162A10916074A9504";
	
	private void startJxta() {
		try {
			manager =
				new NetworkManager(NetworkManager.ConfigMode.ADHOC, "ServiceServer",
					new File(new File(".cache"), "ServiceServer").toURI());
			manager.startNetwork();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		netPeerGroup = manager.getNetPeerGroup();
		groupAdvertisement = netPeerGroup.getPeerGroupAdvertisement();
		discovery = netPeerGroup.getDiscoveryService();
		pipeService = netPeerGroup.getPipeService();
		startServer();
	}
	
	/**
	 * Creates the pipe advertisement pipe ID
	 * 
	 * @return the pre-defined Pipe Advertisement
	 */
	public static PipeAdvertisement createPipeAdvertisement() {
		PipeID pipeID = null;
		
		try {
			pipeID = (PipeID) IDFactory.fromURI(new URI(PIPEIDSTR));
		} catch (URISyntaxException use) {
			use.printStackTrace();
		}
		PipeAdvertisement advertisement =
			(PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		
		advertisement.setPipeID(pipeID);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("Pipe tutorial");
		return advertisement;
	}
	
	private void startServer() {
		try {
			ModuleClassAdvertisement mcadv =
				(ModuleClassAdvertisement) AdvertisementFactory
					.newAdvertisement(ModuleClassAdvertisement.getAdvertisementType());
			mcadv.setName("JXTAMOD:JXTA-EX1");
			mcadv
				.setDescription("Tutorial example to use JXTA module advertisement Framework");
			ModuleClassID mcID = IDFactory.newModuleClassID();
			mcadv.setModuleClassID(mcID);
			discovery.publish(mcadv);
			discovery.remotePublish(mcadv);
			ModuleSpecAdvertisement mdadv =
				(ModuleSpecAdvertisement) AdvertisementFactory
					.newAdvertisement(ModuleSpecAdvertisement.getAdvertisementType());
			mdadv.setName("JXTASPEC:JXTA-EX1");
			mdadv.setVersion("Version 1.0");
			mdadv.setCreator("sun.com");
			mdadv.setModuleSpecID(IDFactory.newModuleSpecID(mcID));
			mdadv.setSpecURI("http://www.jxta.org/Ex1");
			PipeAdvertisement pipeadv = createPipeAdvertisement();
			mdadv.setPipeAdvertisement(pipeadv);
			StructuredTextDocument doc =
				(StructuredTextDocument) mdadv.getDocument(MimeMediaType.XMLUTF8);
			StringWriter out = new StringWriter();
			doc.sendToWriter(out);
			System.out.println(out.toString());
			out.close();
			discovery.publish(mdadv);
			discovery.remotePublish(mdadv);
			serviceInputPipe = pipeService.createInputPipe(pipeadv);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ServiceServer: Error publishing the module");
		}
		while (true) {
			System.out.println("Waiting for client messages to arrive");
			Message msg;
			try {
				msg = serviceInputPipe.waitForMessage();
			} catch (Exception e) {
				serviceInputPipe.close();
				System.out.println("ServiceServer: Error listening for message");
				return;
			}
			String ip = null;
			try {
				Message.ElementIterator en = msg.getMessageElements();
				if (!en.hasNext()) {
					return;
				}
				MessageElement msgElement = msg.getMessageElement(null, "DataTag");
				if (msgElement.toString() != null) {
					ip = msgElement.toString();
				}
				if (ip != null) {
					System.out.println("ServiceServer: receive message: " + ip);
				} else {
					System.out.println("ServiceServer: error could not find the tag");
				}
			} catch (Exception e) {
				System.out.println("ServiceServer: error receiving message");
			}
		}
	}
	
	public static void main(String args[]) {
		System.setProperty("net.jxta.logging.Logging", "FINEST");
		System.setProperty("net.jxta.level", "FINEST");
		System.setProperty("java.util.logging.config.file", "logging.properties");
		ServiceServer myapp = new ServiceServer();
		myapp.startJxta();
	}
	
}
