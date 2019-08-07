package hiaround.android.com.util;

import com.google.commons.codec.binary.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * Created by lake on 17-4-12.
 *
 * 前端代码 依赖 jsencrypt 项目
 * <script src="bin/jsencrypt.min.js"></script>
 * <script type="text/javascript">
 * var encrypt = new JSEncrypt(); encrypt.setPublicKey('java生成的公钥'); 
 * var encrypted = encrypt.encrypt('要加密的字符串');
 * </script>
 */
public class RSAUtil {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    public static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }

    public static String encryptBASE64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data 加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data 加密数据
     * @param publicKey 公钥
     * @param sign 数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(String data, String key)
            throws Exception {
        return decryptByPrivateKey(decryptBASE64(data), key);
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     * https://blog.csdn.net/uestczhh/article/details/52996201
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(String data, String key)
            throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes());
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Key> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        Map<String, Key> keyMap = new HashMap(2);
        keyMap.put(PUBLIC_KEY, keyPair.getPublic());// 公钥
        keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// 私钥
        return keyMap;
    }

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String byteToHex(byte[] a) {
        int hn, ln, cx;
        StringBuilder buf = new StringBuilder(a.length * 2);
        for (cx = 0; cx < a.length; cx++) {
            hn = ((int) (a[cx]) & 0x00ff) / 16;
            ln = ((int) (a[cx]) & 0x000f);
            buf.append(hexDigits[hn]);
            buf.append(hexDigits[ln]);
        }
        return buf.toString();
    }

    public static void main(String[] args) throws Exception {
//        Map<String, Key> keyMap = RSACoder.initKey();
//        String publicKey = RSACoder.getPublicKey(keyMap);
//        String privateKey = RSACoder.getPrivateKey(keyMap);
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKHmixF9NFSyNgsl1Uv5PtBNz8+xH0I3oID7w+O042KmGowTGt7QrBUQrrt5QN/EdKl09At/S"
                + "7rDkQ0PdxW4BWjTByDks14QjNaDK7i4jZyLWZzTfjCbJbD89mxUNWmETlnooHo1+aIxzjJ4k2uHt2S/NyrNuxDmil7YksKCFc8wIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIoeaLEX00VLI2CyXVS/k+0E3Pz7EfQjegg"
                + "PvD47TjYqYajBMa3tCsFRCuu3lA38R0qXT0C39LusORDQ93FbgFaNMHIOSzXhCM1oMruLiNnItZnNN+MJslsPz2bFQ1aYROWeigejX5ojHOMniTa4e"
                + "3ZL83Ks27EOaKXtiSwoIVzzAgMBAAECgYBopClFInvaH9cpx3iWYJ6+D9UthTpQ8R/fQ6ymqV/UAcADjnsI/nILjE7tzyPIhL2ucXyF19j+5rhFWUxfAaf0z"
                + "M8iZYfGvQJBoJcXDBLE+HNpIWPNUlQUztJ5A+zwwUzSRcow/Qv0MV8BMUWKU1NYesdQ1MEamjLOp+jcWPQ5oQJBAM52sUcfpYaCTfv2anHDmlLO0mvyqp9Pc4"
                + "bcnD5rtU0/xB9dp3eKVGUmDcZ22oStIAjBPEgm21TbF2VDKeO1Vk8CQQCrQd9b+np9FdagBILpLA0E9wrq/VVW485WsUql/pBUNcNrWgrgTgmY00kzFndhNd4+"
                + "P19PZG1Pue8pBA2u8IodAkBfWE7L8qBlOp264vQLL3KAFUT1Vu5WfcFzIUQCS4lBcFYoyf5BZR9OSsGHynFlXfHyORKWm2Mkj70BJYAdyStfAkBwPVPuLFNq940"
                + "agtpph31g66g0KqRrthHdr4SFFjnxdusEyJaoO3z+tDsdSHQlOfDWSyJkHiWmQu4Dq6xsIynhAkAUalFQIw010dXrGb/wuz1MswT4ewAc2GPCt49F5tCUCfG0qwIpwVRAhuWUMkUBxQ6TCX+d3PAN4yhhbHuR1Bqg";
        System.err.println("公钥: \n\r" + publicKey);
        System.err.println("私钥： \n\r" + privateKey);
        System.err.println("公钥加密——私钥解密");
        String inputStr = "123456";
        byte[] encodedData = RSAUtil.encryptByPublicKey(inputStr, publicKey);


        String jputStr = Base64.encodeBase64String(encodedData);


        byte[] decodedData = RSAUtil.decryptByPrivateKey(Base64.decodeBase64(jputStr), privateKey);


        String outputStr = new String(decodedData);

        byte[] encodedDataWeb = decryptBASE64("FqIrMRkf+u5h8RlUUcU5WD1nCMnczERiEj52TSD2ps4fA9U4K7rxxwNVUMPfje8/WxYJq1pLbB"
                + "Cyf/zOdQr0OCLHvRoUPl/op5iod2ghc8QQbs4qXTogjTcoeFgjB1KrpQenNLeGHkjTA0sk41xzbYpGX0eIg4k819EbUvB3AzI=");
        byte[] decodedDataWeb = RSAUtil.decryptByPrivateKey(encodedDataWeb,
                privateKey);

        String outputStrWeb = new String(decodedDataWeb);
        System.err.println("加密前: " + inputStr + "\n\r"+"   公钥加密后    "+jputStr + "解密后: " + outputStr + "\n\r" + "web解密后: " + outputStrWeb);

        System.err.println("私钥加密——公钥解密");
        inputStr = "dounine";
        byte[] data = inputStr.getBytes();
        encodedData = RSAUtil.encryptByPrivateKey(data, privateKey);
        decodedData = RSAUtil.decryptByPublicKey(encodedData, publicKey);
        outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = RSAUtil.sign(encodedData, privateKey);
        System.err.println("签名:" + sign);
        // 验证签名
        boolean status = RSAUtil.verify(encodedData, publicKey, sign);
        System.err.println("状态:" + status);
    }
}
