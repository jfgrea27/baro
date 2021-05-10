package com.baro.helpers

object UnicodeHelper {


//    fun getStringEmoji(unicode: String) {
//    try
//    {
//        var emoji = unicode.trim()
//        var unicodeHexEmoji = "U+"
//        var sb = StringBuilder()
//
//        //Firstly you want to encode emojis to unicode types by converting to byte array
////        var utf8Bytes = emoji..
////        ("UTF-8"); // "\\uf0\\u9f\\u98\\u80"
////        byte[] utf16Bytes = emoji . getBytes ("UTF-16"); // "\\ud83d\\ude00"
//
//        //convert emoji to hex
//        for (byte b : utf16Bytes) {
//        sb.append(String.format("%02x", b));
//    }
//        //we are converting our current emoji to hex just for the purpose of this example
//        unicodeHexEmoji += sb; //yields "U+feffd83dde21";
//        byte[] utfHexBytes = getByteFromHex (unicodeHexEmoji.replace("U+", "")); // "\\ud83d\\ude00"
//        //NB: we removed "U+" because its only a prefix denoting that the string is a <hex>
//
//        //Decoding our unicodes back to emoji string
//        String emojiUTF_8 = new String(utf8Bytes, "UTF-8");
//        String emojiUTF_16 = new String(utf16Bytes, "UTF-16");
//        String emojiUTF_hex = new String(utfHexBytes, "UTF-16");
//        Log.d("Tag", "emojiUTF_8 : " + emojiUTF_8);
//        Log.d("Tag", "emojiUTF_16 : " + emojiUTF_16)
//        Log.d("Tag", "emojiUTF_hex : " + emojiUTF_hex)
//        //output
//        //emojiUTF-8 : 😀
//        //emojiUTF-16 : 😀
//        //emojiUTF-hex : 😡
//
//    } catch (UnsupportedEncodingException e)
//    {
//        e.printStackTrace();
//       }
//}
//    public byte[] getByteFromHex(String hexString){
//        //To convert hex string to byte array, you need to first get the length
//        //of the given string and include it while creating a new byte array.
//        byte[] val = new byte[hexString.length() / 2];
//        for (int i = 0; i < val.length; i++) {
//            int index = i * 2;
//            int j = Integer.parseInt(hexString.substring(index, index + 2), 16);
//            val[i] = (byte) j;
//        }
//        return val;
//    }
}