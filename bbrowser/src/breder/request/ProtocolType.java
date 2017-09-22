package breder.request;

public enum ProtocolType {

	/**
	 * Solicita ao God Server as portas disponíveis para que o Master Server
	 * possa comunicar.
	 */
	GOD_INFO,

//	/**
//	 * Solicita ao God Server os ips e portas dos Master Server disponíveis.
//	 */
//	MASTER_INFO,

	/**
	 * Cadastra no God Server o ip e porta do Master Server
	 */
	MASTER_ADDRESS;

}
