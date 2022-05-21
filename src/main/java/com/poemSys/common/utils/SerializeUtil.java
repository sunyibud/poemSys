package com.poemSys.common.utils;

import java.io.*;

/**
 * 封装序列化和反序列化方式，用于redis存储java对象
 */

public class SerializeUtil
{

    /**
     * 序列化操作
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object)
    {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try
        {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 反序列化操作
     *
     * @param bytes
     * @return
     */
    public static Object unSerialize(byte[] bytes)
    {
        ByteArrayInputStream bais = null;

        try
        {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}