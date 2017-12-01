package com.httpmapper;

/**
 * @author zph  on 2017/12/1
 */
public class AutoTest {


    public static void main(String[] args)  throws Exception{
        try (MyResource resource = new MyResource();){
            resource.dosomthind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MyResource implements AutoCloseable{

        public void dosomthind(){
            System.out.println("do work");
        }


        @Override
        public void close() throws Exception {
            System.out.println("resource closed");
        }
    }

}
