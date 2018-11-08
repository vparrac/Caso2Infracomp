package uniandes.isis2203.cliente;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
@SuppressWarnings("deprecation")
/**
 * Clase que crea un Certificado local con el estandar X509
 * @author Valerie Parra Cortés
 * @author Pablo Andrés Suarez Murillo
 */
public class GeneradorCertificados {	
	public static X509Certificate createCertificate(KeyPair pair) throws Exception	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(System.currentTimeMillis()- 31535000000L); 
		Date startDate = gc.getTime();		
		gc.add(GregorianCalendar.YEAR, 4);			 
		Date expiryDate = gc.getTime();              
		BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());       
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		X500Principal subjectName = new X500Principal("CN=Test V3 Certificate");		 
		certGen.setSerialNumber(serialNumber);
		certGen.setIssuerDN(new X500Principal("CN=Test Certificate"));
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
		certGen.setSubjectDN(subjectName);
		certGen.setPublicKey(pair.getPublic());
		certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");		
		Security.addProvider(new BouncyCastleProvider());
		return  certGen.generate(pair.getPrivate(), "BC");  
	}
}