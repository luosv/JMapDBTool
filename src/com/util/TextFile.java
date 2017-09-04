package com.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * <b>读写文本文件的工具类.</b>
 * <p>
 * 用于读取或写入文本文件(字符流), 要读写原始字节流文件，请使用BinaryFile.
 * <p>
 * 本工具类主要用于方便读写小文本, 或者临时处理一些文本文件.<br>
 * 如果需要读取大文件时, 请注意应用环境是否适合并调整JVM的内存大小, 否则极有可能造成内存溢出!
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class TextFile
{

    private TextFile()
    {
        throw new UnsupportedOperationException("该类不允许被实例化!");
    }

    /**
     * 读取目标文件的字符流, 并将其转换为一个String返回.
     *
     * @param file 目标文件
     * @param charset 设置读取时的字符集, 默认为utf-8
     * @return 文本文件的内容
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String read(File file, String charset)
            throws FileNotFoundException, IOException
    {
        if (StringUtils.isEmpty(charset))
            charset = "utf-8";
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), charset));
        StringBuilder result = new StringBuilder();
        String line;
        try
        {
            while (null != (line = in.readLine()))
            {
                result.append(line);
                result.append("\n");
            }

            return result.toString();
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }
    }

    /**
     * 读取目标文件的字符流, 并将其转换为一个String返回.
     *
     * @param filename 目标文件的全路径
     * @param charset 设置读取时的字符集, 默认为utf-8
     * @return 文本文件的内容
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String read(String filename, String charset)
            throws FileNotFoundException, IOException
    {
        return read(new File(filename), charset);
    }

    /**
     * 读取目标文件的字符流, 并将其转为一个String返回.
     *
     * @param file 目标文件
     * @return 文本文件的内容
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String read(File file) throws FileNotFoundException,
            IOException
    {
        return read(file, null);
    }

    /**
     * 读取目标文件的字符流, 并将其转为一个String返回.
     *
     * @param filename 目标文件的全路径
     * @return 文本文件的内容
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String read(String filename) throws FileNotFoundException,
            IOException
    {
        return read(new File(filename), null);
    }

    /**
     * 读取目标文件的字符流, 并将转换为一个List返回.
     *
     * @param file 目标文件
     * @param charset 设置读取时的字符集, 默认为utf-8
     * @return 文本文件的内容, 其中, 文件的每一行作为List中一个元素.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> readLine(File file, String charset)
            throws FileNotFoundException, IOException
    {
        if (StringUtils.isEmpty(charset))
            charset = "utf-8";
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), charset));
        List<String> result = new ArrayList<>();
        String line;
        try
        {
            while (null != (line = in.readLine()))
                result.add(line);

            return result;
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }
    }

    /**
     * 读取目标文件的字符流, 并将转换为一个List返回.
     *
     * @param filename 目标文件的全路径
     * @param charset 设置读取时的字符集, 默认为utf-8
     * @return 文本文件的内容, 其中, 文件的每一行作为List中一个元素.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> readLine(String filename, String charset)
            throws FileNotFoundException, IOException
    {
        return readLine(new File(filename), charset);
    }

    /**
     * 读取目标文件的字符流, 并将转换为一个List返回.
     *
     * @param file 目标文件
     * @return 文本文件的内容, 其中, 文件的每一行作为List中一个元素.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> readLine(File file)
            throws FileNotFoundException, IOException
    {
        return readLine(file, null);
    }

    /**
     * 读取目标文件的字符流, 并将转换为一个List返回.
     *
     * @param filename 目标文件的全路径
     * @return 文本文件的内容, 其中, 文件的每一行作为List中一个元素.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> readLine(String filename)
            throws FileNotFoundException, IOException
    {
        return readLine(new File(filename), null);
    }

    /**
     * 将目标文本写入文件.
     *
     * @param text 文本内容
     * @param file 要写入的文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(String text, File file)
            throws FileNotFoundException, IOException
    {
        BufferedReader in = new BufferedReader(new StringReader(text));
        PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter(file)));
        String line;
        try
        {
            while (null != (line = in.readLine()))
                out.println(line);
            out.flush();
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            out.close();
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }
    }

    /**
     * 将目标文本写入文件.
     * <p>
     * 将List中每一个元素作为文本文件的一行写入.
     *
     * @param text 文本内容
     * @param file 要写入的文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(List<String> text, File file)
            throws FileNotFoundException, IOException
    {
        try (PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter(file))))
        {
            if (null != text && text.size() > 0)
            {
                Iterator<String> it = text.iterator();
                while (it.hasNext())
                    out.println(it.next());
            }
            
            out.flush();
        }
    }

    /**
     * 读取输入文件的内容, 并将其写入输出文件.
     *
     * @param inputFile 输入文件
     * @param outputFile 输出文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(File inputFile, File outputFile)
            throws FileNotFoundException, IOException
    {
        BufferedReader in = new BufferedReader(new FileReader(inputFile));
        PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter(outputFile)));
        String line;
        try
        {
            while (null != (line = in.readLine()))
                out.println(line);
            out.flush();
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            out.close();
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }
    }

    /**
     * 将目标文本追加至目标文件.
     * <p>
     * 注意: 如果待追加文本内容不为空, 将在目标文件最末尾新起一行追加.
     *
     * @param text 目标文本
     * @param file 目标文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void append(String text, File file)
            throws FileNotFoundException, IOException
    {
        BufferedReader in = new BufferedReader(new StringReader(text));
        RandomAccessFile out = new RandomAccessFile(file, "rw");
        String line;
        try
        {
            out.seek(out.length());
            while (null != (line = in.readLine()))
            {
                out.writeBytes("\r\n");
                out.write(line.getBytes());
            }
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            out.close();
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }
    }

    /**
     * 将目标文本追加至目标文件.
     * <p>
     * 注意: 如果待追加文本内容不为空, 将在目标文件最末尾新起一行追加.
     *
     * @param text 目标文本. 其中, List中每一个元素都将作为文本的一行追加.
     * @param file 目标文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void append(List<String> text, File file)
            throws FileNotFoundException, IOException
    {
        try
        (RandomAccessFile out = new RandomAccessFile(file, "rw")) {
            out.seek(out.length());
            if (null != text && text.size() > 0)
            {
                Iterator<String> it = text.iterator();
                while (it.hasNext())
                {
                    out.writeBytes("\r\n");
                    out.write(it.next().getBytes());
                }
            }
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 读取输入文件的内容, 并将其追加至目标文件.
     * <p>
     * 注意: 如果待追加文本内容不为空, 将在目标文件最末尾新起一行追加.
     *
     * @param inputFile 输入文件
     * @param outputFile 输出文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void append(File inputFile, File outputFile)
            throws FileNotFoundException, IOException
    {
        BufferedReader in = new BufferedReader(new FileReader(inputFile));
        RandomAccessFile out = new RandomAccessFile(outputFile, "rw");
        String line;
        try
        {
            out.seek(out.length());
            while (null != (line = in.readLine()))
            {
                out.writeBytes("\r\n");
                out.write(line.getBytes());
            }
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            out.close();
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }
    }

}
