package com.util;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * <b>字符串操作的工具类.</b>
 * <p>
 * 此类封装了一些常用的字符串操作.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.7.0
 */
public class StringUtils
{

    private StringUtils()
    {
        throw new UnsupportedOperationException("该类不允许被实例化!");
    }

    private static final char[] DIGITS =
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 判断字符串是否为空.
     * <p>
     * 如果字符串不等于null, 则先去除首尾空格后再进行判定.
     * 这里只是简单的使用了java.lang.String提供的trim()方法去除半角空格, 如果字符串含有全角空格, 或二者皆有,
     * 可以使用StringUtil.trim(String)去除首尾空格.
     *
     * @see #trim(String)
     * @param source 指定的字符串
     * @return 如果为空则返回true, 反之返回false.
     */
    public static boolean isEmpty(String source)
    {
        return null == source || "".equals(source.trim());
    }

    /**
     * 判断字符串是否不为空.
     * <p>
     * 如果字符串不等于null, 则先去除首尾空格后再进行判定.
     * 这里只是简单的使用了java.lang.String提供的trim()方法去除半角空格, 如果字符串含有全角空格, 或二者皆有,
     * 可以使用StringUtil.trim(String)去除首尾空格.
     *
     * @see #trim(String)
     * @param source 指定的字符串
     * @return 如果不为空则返回true, 反之返回false.
     */
    public static boolean isNotEmpty(String source)
    {
        return !isEmpty(source);
    }

    /**
     * 如果字符串的值为null, 将其替换为空字符串.
     *
     * @param source 指定的字符串
     * @return 原字符串或空字符串: "".
     */
    public static String replaceNull(String source)
    {
        return replaceNull(source, "");
    }

    /**
     * 如果字符串的值为null, 将其替换为默认值.
     *
     * @param source 指定的字符串
     * @param defaultVal 默认值
     * @return 原字符串或默认值.
     */
    public static String replaceNull(String source, String defaultVal)
    {
        return (null == source || source.length() == 0) ? defaultVal : source;
    }

    /**
     * 判断字符串所包含的内容是否由汉字组成.
     * <p>
     * 注意: 此方法并不能准确的"认出"汉字, 它所能"认出"的只是以双字节编码的文字. 例如日文、韩文, 它也会将它们"认作"汉字.
     *
     * @param source 指定的字符串
     * @return 如果字符串的所有内容都是双字节编码的文字, 则返回true, 否则返回false.
     * @throws NullPointerException 如果source为null
     */
    public static boolean isChinese(String source)
    {
        if (null == source)
            throw new NullPointerException("source");
        if ("".equals(source.trim()))
            return false;

        char[] temp = source.toCharArray();
        for (char c : temp)
        {
            // 为了适应不同字符集, 这里将字节数小于2的字符都认为不是汉字, 注: 某些OS下UTF-8编码的汉字会占3个字节.
            if (String.valueOf(c).getBytes().length < 2)
                return false;
        }

        return true;
    }

    /**
     * 获取字符串的长度.
     * <p>
     * 本方法与java.lang.String提供的length()的区别在于: 它以字节数来确定字符串的长度.<br>
     * 例如: 单个英文字母或数字的长度=1，单个汉字的长度>=2(取决于系统字符集编码).
     *
     * @param source 指定的字符串
     * @return 字符串的长度
     * @throws NullPointerException 如果source为null
     */
    public static int length(String source)
    {
        if (null == source)
            throw new NullPointerException("source");

        int count = 0;
        if (source.length() > 0)
        {
            char[] temp = source.toCharArray();
            for (char c : temp)
                count += String.valueOf(c).getBytes().length;
        }

        return count;
    }

    /**
     * 获取字符串的长度.
     * <p>
     * 本方法与StringUtils.length()一样以字节数来确定字符串的长度, 区别在于: 前者完全以统计字节数来获取长度,
     * 后者将所有非单字节字符以指定的字节数来进行统计, 以屏蔽不同系统字符集的编码差异.<br>
     * 例如: 单个英文字母或数字的长度=1，单个汉字的长度<b>始终</b>=3 <i>(bytes=3)</i>.
     *
     * @param source 指定的字符串
     * @param bytes 设置所有非单字节字符的默认字节数.
     * @return 字符串的长度
     * @throws NullPointerException 如果source为null
     * @throws IllegalArgumentException 如果bytes小于1
     */
    public static int length(String source, int bytes)
    {
        if (null == source)
            throw new NullPointerException("source");
        if (bytes < 1)
            throw new IllegalArgumentException("bytes必须大于0! bytes: " + bytes);

        int count = 0;
        if (source.length() > 0)
        {
            int length;
            char[] temp = source.toCharArray();
            for (char c : temp)
            {
                length = String.valueOf(c).getBytes().length;
                if (length > 1)
                    count += bytes;
                else
                    count += length;
            }
        }

        return count;
    }

    /**
     * 随机生成一个指定长度的字符串.
     *
     * @param length 随机字符串的长度
     * @return 由0~9的数字以及26个大写英文字母所组成的随机字符串
     * @throws IllegalArgumentException 如果length小于1
     */
    public static String random(int length)
    {
        if (length < 1)
            throw new IllegalArgumentException("length必须大于0! length: " + length);

        StringBuilder result = new StringBuilder(length);
        for (int i = 0, j; i < length; i++)
        {
            j = ((Double) (Math.random() * 997)).intValue() % DIGITS.length;
            result.append(String.valueOf(DIGITS[j]));
        }

        return result.toString();
    }

    /**
     * 过滤字符串中所包含的标签('<' and '>').
     *
     * @param source 指定的字符串
     * @return 拆分后不含标签的字符串
     * @throws NullPointerException 如果source为null
     */
    public static String splitTags(String source)
    {
        if (null == source)
            throw new NullPointerException("source");
        if ("".equals(source))
            return source;

        Pattern pattern = Pattern.compile("<[^<]*>|<\\/[^>]*>");
        Matcher matcher = pattern.matcher(source);

        return matcher.replaceAll("");
    }

    /**
     * 从指定的索引(beginIndex)开始, 最多截取maxLength个字符.
     *
     * @param source 指定的字符串
     * @param beginIndex 开始截取的索引编号
     * @param maxLength 最多截取的字符长度
     * @return 截取后的子字符串
     * @throws NullPointerException 如果source为null
     * @throws StringIndexOutOfBoundsException 如果beginIndex小于0, 或者大于等于source.length
     * @throws IllegalArgumentException 如果maxLength小于1
     */
    public static String substring(String source, int beginIndex, int maxLength)
    {
        if (null == source)
            throw new NullPointerException("source");

        int count = source.length();
        if (beginIndex < 0 || beginIndex >= count)
            throw new StringIndexOutOfBoundsException("beginIndex: "
                    + beginIndex);
        if (maxLength < 1)
            throw new IllegalArgumentException("maxLength必须大于0! maxLength: "
                    + maxLength);
        if (beginIndex == count - 1)
            return "";

        int endIndex = beginIndex + maxLength;
        if (beginIndex == 0 && endIndex == count)
            return source;

        return (endIndex > count) ? source.substring(beginIndex, count)
                : source.substring(beginIndex, endIndex);
    }

    /**
     * 从字符串的起始处开始, 最多截取maxLength个字符, 超出部分以指定的字符串替代.
     *
     * @param source 指定的字符串
     * @param maxLength 最多截取的字符长度
     * @param replaceStr 如果maxLength小于source.length, 那么, 超出部分以此值替代.
     * @return 截取后的子字符串
     * @throws NullPointerException 如果source为null
     * @throws IllegalArgumentException 如果maxLength小于1
     */
    public static String substring(String source, int maxLength,
                                   String replaceStr)
    {
        if (null == source)
            throw new NullPointerException("source");
        if (maxLength < 1)
            throw new IllegalArgumentException("maxLength必须大于0! maxLength: "
                    + maxLength);
        if (maxLength >= source.length())
            return source;

        StringBuilder result = new StringBuilder(maxLength
                + replaceStr.length());
        result.append(source.substring(0, maxLength));
        result.append(replaceStr);

        return result.toString();
    }

    /**
     * 从字符串的起始处开始, 最多截取maxBytes个字节, 超出部分以指定的字符串替代.
     * <p>
     * 注意: 系统的字符集编码将影响字符串的截取结果, 出现这样的情况, 可以调整maxBytes的值, 或者修改系统字符集. 另外,
     * StringUtils.substringByBytes2()提供了另一种实现以规避这个问题.<br>
     * 举个例子, 在LINUX上, 某些UTF-8字符集会将汉字以3个字节进行编码, 而不是人们通常认为的2个字节.
     *
     * @param source 指定的字符串
     * @param maxBytes 最多截取的字节数
     * @param replaceStr 如果maxBytes小于指定字符串的字节总数, 那么, 超出部分以此值替代.
     * @return 截取后的子字符串
     * @throws NullPointerException 如果source为null
     * @throws IllegalArgumentException 如果maxBytes小于1
     */
    public static String substringByBytes(String source, int maxBytes,
                                          String replaceStr)
    {
        if (null == source)
            throw new NullPointerException("source");
        if (maxBytes < 1)
            throw new IllegalArgumentException("maxBytes必须大于0! maxBytes: "
                    + maxBytes);

        int count = 0; // 统计字节数
        int endIndex = 0; // 计算对原字符串进行substring时的endIndex
        char[] temp = source.toCharArray();
        for (char c : temp)
        {
            count += String.valueOf(c).getBytes().length;
            if (count <= maxBytes)
                endIndex += 1;
            else
                break;
        }

        if (endIndex == source.length())
            return source;

        StringBuilder result = new StringBuilder(endIndex + replaceStr.length());
        result.append(source.substring(0, endIndex));
        result.append(replaceStr);

        return result.toString();
    }

    /**
     * 从字符串的起始处开始, 最多截取maxBytes个字节, 超出部分以指定的字符串替代.
     * <p>
     * 出于解决不同字符集对非单字节字符的编码问题(在LINUX上, 某些UTF-8字符集会将汉字以3个字节进行编码,
     * 而Windows中文系统通常默认是2个字节), 本方法会将所有字节数>2的字符当作双字节处理.
     *
     * @param source 指定的字符串
     * @param maxBytes 最多截取的字节数
     * @param replaceStr 如果maxBytes小于指定字符串的字节总数, 那么, 超出部分以此值替代.
     * @return 截取后的子字符串
     * @throws NullPointerException 如果source为null
     * @throws IllegalArgumentException 如果maxBytes小于1
     */
    public static String substringByBytes2(String source, int maxBytes,
                                           String replaceStr)
    {
        if (null == source)
            throw new NullPointerException("source");
        if (maxBytes < 1)
            throw new IllegalArgumentException("maxBytes必须大于0! maxBytes: "
                    + maxBytes);

        int length; // 纪录单个字符的字节数
        int count = 0; // 统计字节数
        int endIndex = 0; // 计算对原字符串进行substring时的endIndex
        char[] temp = source.toCharArray();
        for (char c : temp)
        {
            length = String.valueOf(c).getBytes().length;
            if (length > 2)
                count += 2;
            else
                count += length;
            if (count <= maxBytes)
                endIndex += 1;
            else
                break;
        }

        if (endIndex == source.length())
            return source;

        StringBuilder result = new StringBuilder(endIndex + replaceStr.length());
        result.append(source.substring(0, endIndex));
        result.append(replaceStr);

        return result.toString();
    }

    /**
     * 从字符串的起点开始向后检索, 返回key在字符串中第一次出现处的索引之后的字符串.
     *
     * @param source 指定的字符串
     * @param key 关键字
     * @return key之后的字符串. 如果没有找到则返回null; 如果该索引已是末位则返回空字符串: "".
     * @throws NullPointerException 如果source为null
     */
    public static String indexOfAfter(String source, char key)
    {
        if (null == source)
            throw new NullPointerException("source");

        int start = -1;
        String ext = null;
        for (int i = 0; i < source.length(); i++)
        {
            if (source.charAt(i) == key)
            {
                start = i;
                break;
            }
        }

        if (start >= 0)
            ext = source.substring(start + 1);

        return ext;
    }

    /**
     * 从字符串的起点开始向后检索, 返回key在源字符串中第一次出现处的索引顺移key.length()之后的字符串.
     *
     * @param source 指定的字符串
     * @param key 关键字, 如果为null或者空字符串: "", 则直接返回null.
     * @return key之后的字符串. 如果没有找到则返回null; 如果该索引已是末位则返回空字符串: "".
     * @throws NullPointerException 如果source为null
     */
    public static String indexOfAfter(String source, String key)
    {
        if (null == source)
            throw new NullPointerException("source");
        if (null == key || "".equals(key))
            // 因为String的indexOf方法在检索空字符串""时, 总是返回起始索引, 所以, key不能为空字符串.
            return null;

        String ext = null;
        int index = source.indexOf(key);
        if (index != -1)
            ext = source.substring(index + key.length(), source.length());

        return ext;
    }

    /**
     * 从字符串的起点开始向后检索, 返回key在源字符串中第一次出现处的索引之前的字符串.
     *
     * @param source 指定的字符串
     * @param key 关键字
     * @return key之前的字符串. 如果没有找到则返回null; 如果该索引已是首位则返回空字符串: "".
     * @throws NullPointerException 如果source为null
     */
    public static String indexOfBefore(String source, char key)
    {
        if (null == source)
            throw new NullPointerException("source");

        int start = -1;
        String ext = null;
        for (int i = 0; i < source.length(); i++)
        {
            if (source.charAt(i) == key)
            {
                start = i;
                break;
            }
        }

        if (start >= 0)
            ext = source.substring(0, start);

        return ext;
    }

    /**
     * 从字符串的起点开始向后检索, 返回key在源字符串中第一次出现处的索引之前的字符串.
     *
     * @param source 指定的字符串
     * @param key 关键字, 如果为null或者空字符串: "", 则直接返回null.
     * @return key之前的字符串. 如果没有找到则返回null; 如果该索引已是首位则返回空字符串: "".
     * @throws NullPointerException 如果source为null
     */
    public static String indexOfBefore(String source, String key)
    {
        if (null == source)
        {
            throw new NullPointerException("source");
        }
        if (null == key || "".equals(key))
            return null;

        String ext = null;
        int index = source.indexOf(key);
        if (index != -1)
            ext = source.substring(0, index);

        return ext;
    }

    /**
     * 从字符串的起点开始向后检索, 返回第一次匹配到beginKey和endKey之间的字符串.
     *
     * @param source 指定的字符串
     * @param beginKey 起始关键字
     * @param endKey 结束关键字
     * @return beginKey和endKey之间的字符串, 如果没有找到则返回null.
     * @throws NullPointerException 如果source为null
     */
    public static String indexOfInner(String source, String beginKey,
                                      String endKey)
    {
        if (null == source)
        {
            throw new NullPointerException("source");
        }

        if (beginKey == null || "".equals(beginKey) || endKey == null
                || "".equals(endKey))
        {
            return null;
        }

        String result = null;
        int index = source.indexOf(beginKey);
        if (index != -1)
        {
            result = source.substring(index + beginKey.length(), source
                    .length());
            index = result.indexOf(endKey);
            if (index != -1)
            {
                return result.substring(0, index);
            }
        }

        return result;
    }

    /**
     * 从字符串的末尾开始向前检索, 返回key在源字符串中第一次出现处的索引之后的字符串.
     *
     * @param source 指定的字符串
     * @param key 关键字
     * @return key之后的字符串. 如果没有找到则返回null; 如果该索引已是末位则返回空字符串: "".
     * @throws NullPointerException 如果source为null
     */
    public static String lastIndexOfAfter(String source, char key)
    {
        if (null == source)
            throw new NullPointerException("source");

        int start = -1;
        String ext = null;
        for (int i = source.length(); i >= 0; i--)
        {
            if (source.charAt(i) == key)
            {
                start = i;
                break;
            }
        }

        if (start >= 0)
            ext = source.substring(start + 1);

        return ext;
    }

    /**
     * 从字符串的末尾开始向前检索, 返回key在源字符串中第一次出现处的索引顺移key.length()之后的字符串.
     *
     * @param source 指定的字符串
     * @param key 关键字, 如果为null或者空字符串: "", 则直接返回null.
     * @return key之后的字符串. 如果没有找到则返回null; 如果该索引已是末位则返回空字符串: "".
     * @throws NullPointerException 如果source为null
     */
    public static String lastIndexOfAfter(String source, String key)
    {
        if (null == source)
            throw new NullPointerException("source");
        if (null == key || "".equals(key))
            return null;

        String ext = null;
        int index = source.lastIndexOf(key);
        if (index != -1)
            ext = source.substring(index + key.length(), source.length());

        return ext;
    }

    /**
     * 从字符串的末尾开始向前检索, 返回key在源字符串中第一次出现处的索引之前的字符串.
     *
     * @param source 指定的字符串
     * @param key 关键字
     * @return key之前的字符串. 如果没有找到则返回null; 如果该索引已是首位则返回空字符串: "".
     * @throws NullPointerException 如果source为null
     */
    public static String lastIndexOfBefore(String source, char key)
    {
        if (null == source)
            throw new NullPointerException("source");

        int start = -1;
        String ext = null;
        for (int i = source.length(); i >= 0; i--)
        {
            if (source.charAt(i) == key)
            {
                start = i;
                break;
            }
        }

        if (start >= 0)
            ext = source.substring(0, start);

        return ext;
    }

    /**
     * 从字符串的末尾开始向前检索, 返回key在源字符串中第一次出现处的索引之前的字符串.
     *
     * @param source 指定的字符串
     * @param key 关键字, 如果为null或者空字符串: "", 则直接返回null.
     * @return key之前的字符串. 如果没有找到则返回null; 如果该索引已是首位则返回空字符串: "".
     * @throws NullPointerException 如果source为null
     */
    public static String lastIndexOfBefore(String source, String key)
    {
        if (null == source)
            throw new NullPointerException("source");
        if (null == key || "".equals(key))
            return null;

        String ext = null;
        int index = source.lastIndexOf(key);
        if (index != -1)
            ext = source.substring(0, index);

        return ext;
    }

    /**
     * 从字符串的末尾开始向前检索, 返回第一次匹配到beginKey和endKey之间的字符串.
     *
     * @param source 指定的字符串
     * @param beginKey 起始关键字
     * @param endKey 结束关键字
     * @return beginKey和endKey之间的字符串, 如果没有找到则返回null.
     * @throws NullPointerException 如果source为null
     */
    public static String lastIndexOfInner(String source, String beginKey,
                                          String endKey)
    {
        if (null == source)
        {
            throw new NullPointerException("source");
        }

        if (beginKey == null || "".equals(beginKey) || endKey == null
                || "".equals(endKey))
        {
            return null;
        }

        String result = null;
        int index = source.lastIndexOf(beginKey);
        if (index != -1)
        {
            result = source.substring(index + beginKey.length(), source
                    .length());
            index = result.indexOf(endKey);
            if (index != -1)
            {
                return result.substring(0, index);
            }
        }

        return result;
    }

    /**
     * 取出Array中的所有元素, 将其连接并创建一个新的String, 其中Array的元素以separator分隔.
     * <p>
     * 如果需要将String按分隔符转换为Array, 建议使用java.lang.String提供的split()方法.
     *
     * @param source 指定的字符串数组, 如果array为空或无可用元素, 直接返回null.
     * @param separator 分隔符, null表示不设置分隔符
     * @return 连接后的String, 格式大致如下:<br>
     * Array[0] + separator + Array[1] + separator......Array[length]
     */
    public static String concat(String[] source, String separator)
    {
        if (null == source || source.length < 1)
            return null;
        if (null == separator)
            separator = "";

        int length = source.length - 1;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            result.append(source[i]);
            result.append(separator);
        }
        result.append(source[length]);

        return result.toString();
    }

    /**
     * 取出java.util.List中的所有元素, 将其连接并创建一个新的String, 其中List的元素以separator分隔.
     *
     * @param source 指定的字符串列表, 如果list为空或无可用元素, 直接返回null.
     * @param separator 分隔符, null表示不设置分隔符
     * @return 连接后的String格式为: list[0] + separator + list[1] +
     * separator...list[size-1]
     */
    public static String concat(List<String> source, String separator)
    {
        if (null == source || source.size() < 1)
            return null;
        if (null == separator)
            separator = "";

        StringBuilder result = new StringBuilder();
        Iterator<String> it = source.iterator();
        while (it.hasNext())
        {
            result.append(it.next());
            if (it.hasNext())
                result.append(separator);
        }

        return result.toString();
    }

    /**
     * 判断一个字符串中是否包含另一个字符串(大小写敏感).
     *
     * @param source 指定的字符串
     * @param containedStr 要匹配的字符串, 如果为null或空字符串: "", 则直接返回false
     * @return 如果包含返回true, 否则返回false
     * @throws NullPointerException 如果source为null
     */
    public static boolean contains(String source, String containedStr)
    {
        if (null == source)
            throw new NullPointerException("source");
        // 因为String的indexOf方法在检索空字符串""时, 总是返回起始索引, 所以, containedStr不能为空字符串.
        if (null == containedStr || "".equals(containedStr))
            return false;

        return source.indexOf(containedStr) != -1;
    }

    /**
     * 判断一个字符串中是否包含另一个字符串(忽略大小写检查).
     *
     * @param source 指定的字符串
     * @param containedStr 要匹配的字符串, 如果为null或空字符串: "", 则直接返回false
     * @return 如果包含返回true, 否则返回false
     * @throws NullPointerException 如果source为null
     */
    public static boolean containsIgnoreCase(String source, String containedStr)
    {
        if (null == source)
            throw new NullPointerException("source");
        if (null == containedStr || "".equals(containedStr))
            return false;

        return source.toLowerCase().indexOf(containedStr.toLowerCase()) != -1;
    }

    /**
     * 判断字符串是否由数字, 或者自定义字符(可选)所组成.
     *
     * @param source 指定的字符串
     * @param defineChar 自定义字符(可选, 如果不传入此参数或者参数无可用值, 则只判断字符串是否由数字组成)
     * @return 如果字符串的所有内容都是数字或者自定义字符(可选), 则返回true, 否则返回false.
     * @throws NullPointerException 如果source为null
     */
    public static boolean isNumber(String source, char... defineChar)
    {
        if (null == source)
            throw new NullPointerException("source");
        if ("".equals(trim(source)))
            return false;

        if (null != defineChar && defineChar.length > 0)
            return isNumber(source.toCharArray(), defineChar);
        else
            return isNumber(source.toCharArray());
    }

    /**
     * 判断字符串是否由字母(包括大小写: a~z, A~Z), 或者自定义字符(可选)所组成.
     *
     * @param source 指定的字符串
     * @param defineChar 自定义字符(可选, 如果不传入此参数或者参数无可用值, 则只判断字符串是否由字母组成)
     * @return 如果字符串的所有内容都是字母或者自定义字符(可选), 则返回true, 否则返回false.
     * @throws NullPointerException 如果source为null
     */
    public static boolean isLetter(String source, char... defineChar)
    {
        if (null == source)
            throw new NullPointerException("source");
        if ("".equals(trim(source)))
            return false;

        if (null != defineChar && defineChar.length > 0)
            return isLetter(source.toCharArray(), defineChar);
        else
            return isLetter(source.toCharArray());
    }

    /**
     * 判断字符串所包含的内容是否由数字, 或者字母(包括大小写: a~z, A~Z), 又或是自定义字符(可选)所组成.
     *
     * @param source 指定的字符串
     * @param defineChar 自定义字符(可选, 如果不传入此参数或者参数无可用值, 则只判断字符串是否由数字或字母组成)
     * @return 如果所有内容都是数字或者字母, 又或是自定义字符(可选), 则返回true, 否则返回false.
     * @throws NullPointerException 如果source为null
     */
    public static boolean isNumberOrLetter(String source, char... defineChar)
    {
        if (null == source)
            throw new NullPointerException("source");
        if ("".equals(trim(source)))
            return false;

        if (null != defineChar && defineChar.length > 0)
            return isNumberOrLetter(source.toCharArray(), defineChar);
        else
            return isNumberOrLetter(source.toCharArray());
    }

    /**
     * 将字符串的编码由ISO-8859-1转换为GBK.
     * <p>
     * 当转换出错时, 直接返回null, 而不抛出异常. 如果需要捕获异常, 请使用transCharacterEncoding().
     *
     * @see #transCharacterEncoding(String, String)
     * @param source 指定的字符串
     * @return 转换后的字符串, 如果出错, 返回null.
     * @throws NullPointerException 如果source为null
     */
    public static String toChinese(String source)
    {
        if (null == source)
            throw new NullPointerException("source");

        String result;
        try
        {
            result = new String(source.getBytes("ISO-8859-1"), "GBK");
        }
        catch (UnsupportedEncodingException e)
        {
            result = null;
        }

        return result;
    }

    /**
     * 将字符串的编码由ISO-8859-1转换为指定编码.
     *
     * @param source 指定的字符串
     * @param encode 转换后的编码格式
     * @return 转换后的字符串, 如果出错, 抛出异常.
     * @throws NullPointerException 如果source或者encode为null
     */
    public static String transCharacterEncoding(String source, String encode)
    {
        if (null == source || null == encode)
            throw new NullPointerException("source or encode");

        String result;
        try
        {
            result = new String(source.getBytes("ISO-8859-1"), encode);
        }
        catch (UnsupportedEncodingException e)
        {
            result = null;
        }

        return result;
    }

    /**
     * 将字符串由指定的编码格式(sourceEncode)转换到另一种编码格式(transEncode).
     *
     * @param source 指定的字符串
     * @param sourceEncode 转换前的编码格式
     * @param transEncode 转换后的编码格式
     * @return 转换后的字符串, 如果出错, 抛出异常.
     * @throws NullPointerException 如果source或者sourceEncode又或者transEncode为null
     */
    public static String transCharacterEncoding(String source,
                                                String sourceEncode, String transEncode)
    {
        if (null == source || null == sourceEncode || null == transEncode)
            throw new NullPointerException(
                    "source or sourceEncode or transEncode");

        String result;
        try
        {
            result = new String(source.getBytes(sourceEncode), transEncode);
        }
        catch (UnsupportedEncodingException e)
        {
            result = null;
        }

        return result;
    }

    /**
     * 将字符串解析为int类型.
     * <p>
     * 如果转换失败, 返回指定的默认值(defaultVal)
     *
     * @param source 源字符
     * @param defaultVal 默认值
     * @return 转换后的数字
     * @throws NullPointerException 如果source为null
     */
    public static int parseInt(String source, int defaultVal)
    {
        if (null == source)
            throw new NullPointerException("source");

        int result;
        try
        {
            result = Integer.parseInt(source);
        }
        catch (NumberFormatException e)
        {
            result = defaultVal;
        }

        return result;
    }

    /**
     * 将字符串解析为long类型.
     * <p>
     * 如果转换失败, 返回指定的默认值(defaultVal)
     *
     * @param source 源字符
     * @param defaultVal 默认值
     * @return 转换后的数字
     * @throws NullPointerException 如果source为null
     */
    public static long parseLong(String source, long defaultVal)
    {
        if (null == source)
            throw new NullPointerException("source");

        long result;
        try
        {
            result = Long.parseLong(source);
        }
        catch (NumberFormatException e)
        {
            result = defaultVal;
        }

        return result;
    }

    /**
     * 将字符串解析为float类型.
     * <p>
     * 如果转换失败, 返回指定的默认值(defaultVal)
     *
     * @param source 源字符
     * @param defaultVal 默认值
     * @return 转换后的数字
     * @throws NullPointerException 如果source为null
     */
    public static float parseFloat(String source, float defaultVal)
    {
        if (null == source)
            throw new NullPointerException("source");

        float result;
        try
        {
            result = Float.parseFloat(source);
        }
        catch (NumberFormatException e)
        {
            result = defaultVal;
        }

        return result;
    }

    /**
     * 将字符串解析为double类型.
     * <p>
     * 如果转换失败, 返回指定的默认值(defaultVal)
     *
     * @param source 源字符
     * @param defaultVal 默认值
     * @return 转换后的数字
     * @throws NullPointerException 如果source为null
     */
    public static double parseDouble(String source, double defaultVal)
    {
        if (null == source)
            throw new NullPointerException("source");

        double result;
        try
        {
            result = Double.parseDouble(source);
        }
        catch (NumberFormatException e)
        {
            result = defaultVal;
        }

        return result;
    }

    /**
     * 去除字符串首尾的空格, 包括全角空格.
     *
     * @param source 指定的字符串
     * @return 去除首尾空格后的字符串
     */
    public static String trim(String source)
    {
        if (null == source || "".equals(source))
            return source;

        // 发现对于某些XML中的空格不能正确剔除, 故先trim一次.
        source = source.trim();
        boolean hasStart = true, hasEnd = true;
        // System.out.println("显示原始字符串: '" + source + "' \n");
        for (int i = 1; i <= source.length(); i++)
        {
            if (hasStart && source.length() > 0)
            {
                char start = source.charAt(0);
                // System.out.println("start = '" + start + "'");
                if (start == ' ' || start == '　')
                {
                    source = source.substring(1, source.length());
                    // System.out.println("去除头部空格后: '" + source + "'");
                }
                else
                {
                    hasStart = false;
                }
            }

            if (hasEnd && source.length() > 1)
            {
                char end = source.charAt(source.length() - 1);
                // System.out.println("end = '" + end + "'");
                if (end == ' ' || end == '　')
                {
                    source = source.substring(0, source.length() - 1);
                    // System.out.println("去除尾部空格后: '" + source + "'");
                }
                else
                {
                    hasEnd = false;
                }
            }

            // System.out.println("第" + i + "次迭代后 = '" + source + "'\n");
            if (!hasStart && !hasEnd)
                break;
        }

        return source;
    }

    private static boolean isNumber(char[] sourceChar)
    {
        for (char c : sourceChar)
        {
            if (!(c >= '0' && c <= '9'))
                return false;
        }

        return true;
    }

    private static boolean isNumber(char[] sourceChar, char[] defineChar)
    {
        boolean flag;
        for (char c1 : sourceChar)
        {
            if (!(c1 >= '0' && c1 <= '9'))
            {
                flag = false;
                for (char c2 : defineChar)
                {
                    if (c1 == c2)
                    {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                    return false;
            }
        }

        return true;
    }

    private static boolean isLetter(char[] sourceChar)
    {
        for (char c : sourceChar)
        {
            if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z'))
                return false;
        }

        return true;
    }

    private static boolean isLetter(char[] sourceChar, char[] defineChar)
    {
        boolean flag;
        for (char c1 : sourceChar)
        {
            if (!(c1 >= 'a' && c1 <= 'z') && !(c1 >= 'A' && c1 <= 'Z'))
            {
                flag = false;
                for (char c2 : defineChar)
                {
                    if (c1 == c2)
                    {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                    return false;
            }
        }

        return true;
    }

    private static boolean isNumberOrLetter(char[] sourceChar)
    {
        for (char c : sourceChar)
        {
            if (!(c >= '0' && c <= '9') && !(c >= 'a' && c <= 'z')
                    && !(c >= 'A' && c <= 'Z'))
                return false;
        }

        return true;
    }

    private static boolean isNumberOrLetter(char[] sourceChar, char[] defineChar)
    {
        boolean flag;
        for (char c1 : sourceChar)
        {
            if (!(c1 >= '0' && c1 <= '9') && !(c1 >= 'a' && c1 <= 'z')
                    && !(c1 >= 'A' && c1 <= 'Z'))
            {
                flag = false;
                for (char c2 : defineChar)
                {
                    if (c1 == c2)
                    {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                    return false;
            }
        }

        return true;
    }

}
