import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Inicializa
 * 
 * @author bernardobreder
 */
public class Main {
	
	/**
	 * Inicializador
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Timezone Atual: " + TimeZone.getDefault().getID());
		System.out.println("Horas de Diferença: "
			+ TimeZone.getDefault().getRawOffset() / 60 / 60 / 1000);
		System.out.println("Horário: " + new Date());
		System.out.println("Mudando para GMT-00:00");
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-00:00"));
		System.out.println("Horas de Diferença: "
			+ TimeZone.getDefault().getRawOffset() / 60 / 60 / 1000);
		System.out.println("Horário: " + new Date());
		System.out.println("Mudando para America/Sao_Paulo");
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
		System.out.println("Horário: " + new Date());
		// new Server(8080).start();
	}
	
}
