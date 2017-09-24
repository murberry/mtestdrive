package com.mtestdrive.utils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
  
/**
 * @ClassName:  ChineseToEnglish   
 * @Description:TODO
 * @author: mengtaocui
 * @date:   2017年3月31日 下午12:57:41   
 *     
 * @Copyright:
 */
public class ChineseToEnglish {  
    
	/**
	 * @Title: getPingYin   
	 * @Description: 将中文转换成拼音
	 * @param: @param src
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
    public static String getPingYin(String src) {  
  
        char[] t1 = null;  
        t1 = src.toCharArray();  
        String[] t2 = new String[t1.length];  
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();  
          
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);  
        String t4 = "";  
        int t0 = t1.length;  
        try {  
            for (int i = 0; i < t0; i++) {  
                // �ж��Ƿ�Ϊ�����ַ�  
                if (java.lang.Character.toString(t1[i]).matches(  
                        "[\\u4E00-\\u9FA5]+")) {  
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);  
                    t4 += t2[0];  
                } else  
                    t4 += java.lang.Character.toString(t1[i]);  
            }  
            // System.out.println(t4);  
            return t4;  
        } catch (BadHanyuPinyinOutputFormatCombination e1) {  
            e1.printStackTrace();  
        }  
        return t4;  
    }  
  
    
    /**
     * @Title: getPinYinHeadChar   
     * @Description: 返回中文字符串的首字母
     * @param: @param str
     * @param: @return      
     * @return: String      
     * @throws
     */
    public static String getPinYinHeadChar(String str) {  
  
        String convert = "";  
        for (int j = 0; j < str.length(); j++) {  
            char word = str.charAt(j);  
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
            if (pinyinArray != null) {  
                convert += pinyinArray[0].charAt(0);  
            } else {  
                convert += word;  
            }  
        }  
        return convert;  
    }  
  
    /**
     * @Title: getCnASCII   
     * @Description: 返回指定中文的ASC
     * @param: @param cnStr
     * @param: @return      
     * @return: String      
     * @throws
     */
    public static String getCnASCII(String cnStr) {  
        StringBuffer strBuf = new StringBuffer();  
        byte[] bGBK = cnStr.getBytes();  
        for (int i = 0; i < bGBK.length; i++) {  
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));  
        }  
        return strBuf.toString();  
    }  
  
    public static void main(String[] args) {  
        System.out.println(getPingYin("崔梦涛"));  
        System.out.println(getPinYinHeadChar("崔梦涛"));  
        System.out.println(getCnASCII("崔梦涛"));  
    }  
}  