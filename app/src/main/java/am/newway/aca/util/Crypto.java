package am.newway.aca.util;

import android.util.Log;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto
{
    private static final String SALT = "armeniancodeacademy";
    private final static String HEX = "0A1B2C3D4E5F6789";

    public static String encrypt( String seed, String cleartext)
            throws Exception
    {
        SecretKey key = generateKey(seed.toCharArray(), SALT.getBytes());
        byte[] rawKey = key.getEncoded();
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    public static String decrypt( String seed, String encrypted)
            throws Exception
    {
        SecretKey key = generateKey(seed.toCharArray(), SALT.getBytes());
        byte[] rawKey = key.getEncoded();
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    public static String decryptDes( String keyStr, String encrypted )
    {
        try
        {
            KeySpec ks = new DESKeySpec(keyStr.getBytes("UTF-8"));
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(ks);
            IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex("0A1B2C3D4E5F6789".toCharArray()));
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
            cipher.init( Cipher.DECRYPT_MODE, key, iv);
            byte[] decoded = cipher.doFinal(Base64.decodeBase64(encrypted.getBytes()));
            return new String(decoded);
        } catch ( Exception e)
        {
            Log.e("Crypto", e.getMessage());
        }
        return "";
    }

    private static SecretKey generateKey( char[] passphraseOrPin, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        final int outputKeyLength = 128;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
        return secretKeyFactory.generateSecret(keySpec);
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception
    {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init( Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception
    {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init( Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encrypted);
    }

    public static String toHex( String txt)
    {
        return toHex(txt.getBytes());
    }

    public static String fromHex( String hex)
    {
        return new String(toByte(hex));
    }

    private static byte[] toByte( String hexString)
    {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];

        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.
                    substring(2 * i, 2 * i + 2), 16).byteValue();

        return result;
    }

    private static String toHex( byte[] buf)
    {
        if (buf == null)
            return "";

        StringBuffer result = new StringBuffer(2 * buf.length);

        for (byte aBuf : buf)
        {
            appendHex(result, aBuf);
        }

        return result.toString();
    }

    private static void appendHex( StringBuffer sb, byte b)
    {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    /**********************************************************************/
    public static String md5( String txt)
    {
        try
        {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(txt.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append( Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch ( NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    /**********************************************************************/
    public static String sha256Digest( String data) throws SignatureException
    {
        return getDigest(data, true);
    }

    private static String getDigest( String data, boolean toLower)
            throws SignatureException
    {
        try
        {
            MessageDigest mac = MessageDigest.getInstance("SHA-1");
            mac.update(data.getBytes("UTF-8"));
            return toLower ?
                    new String(tooHex(mac.digest())).toLowerCase() : new String(tooHex(mac.digest()));
        } catch ( Exception e)
        {
            throw new SignatureException(e);
        }
    }

    private static String tooHex( byte[] bytes)
    {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}