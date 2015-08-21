package connection;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import clientAPI.data.EncryptFunction;

public enum LocalKeyStore {

    INSTANCE;

    private final String modulus = "21427780265291959354609447428854758377369130561840492109118851194667954601297857896203616882162261052209681459527842410190196742499253281053942795607469922285356508159466219674913066168742856549743343579822338857369369336867515794004471511320531624811868297669786096094581235635568379844807350072772987807604775557563374277660707183023443547282451676685222408738177859413948068673898009237180365905662068237287947704951813059932167530609365927598956374547574234378238342896844754750131233418372036987371681273224661100834590147726793730849677722654287451757224681598115297405268808460948526667134976906348210711049459";
    private final String exponent = "65537";
    private final String privExponent = "19930644207267150744438755151565197692412949338368749225440086228002032082425421084834216975187254668669572339440288961050612829840692761439892655059586974116464928580568866730905453217483666471752670063304852710186948075694188439674147037354120066302727736841094661729000152003799034111719585054794937070295466277659029146882993285706871707170104237643062634390771503682582595501952854648772149803045975549628264384849790124537580078372100990074506416639082376824062875965469867141454727904499122206118413999197889134393860500387141893612482875480591754183392545866689471914911956120217991856773940766124982487051313";

    private RSAPublicKey mPublicKey;
    private RSAPrivateKey mPrivateKey;

    private LocalKeyStore() {
	RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(exponent));
	RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privExponent));
	try {
	    mPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(publicSpec);
	    mPrivateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(privateSpec);
	} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}
    }

    public EncryptFunction getEncryptionFunc() {
	return (plaintext -> {
	    try {
		Cipher sendCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		sendCipher.init(Cipher.ENCRYPT_MODE, mPrivateKey);
		return sendCipher.doFinal(plaintext);
	    } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException
		    | InvalidKeyException e) {
		e.printStackTrace();
	    }
	    return null;
	});
    }

    public RSAPublicKey getPublicKey() {
	return mPublicKey;
    }
}
