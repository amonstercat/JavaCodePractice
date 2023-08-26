package com.example.codepractice;


import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

//带虚拟节点的一致性哈希
public class ConsistentHash {


    /**
     * 待添加入Hash环的服务器列表
     */
    private static String[] servers = {"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111",
            "192.168.0.3:111", "192.168.0.4:111"};

    /**
     * 真实结点列表,考虑到服务器上线、下线的场景，即添加、删除的场景会比较频繁，
     * 这里使用LinkedList会更好
     */
    private static List<String> realNodes = new LinkedList<String>();

    /**
     * 虚拟节点列表，key表示虚拟节点的hash值，value表示虚拟节点的名称
     */
    private static SortedMap<Integer, String> virtualNodes =
            new TreeMap<Integer, String>();


    /**
     * 虚拟节点的数目，这里写死，为了演示需要，一个真实结点对应5个虚拟节点
     */
    private static final int VIRTUAL_NODES = 5;

    static {
        for (int i = 0; i < servers.length; i++) {
            //先把真实节点用List存起来
            realNodes.add(servers[i]);
        }
        for(String  str : realNodes)
        {
            //针对每个真实Server节点，将其对应的所有虚拟节点映射到哈希环上
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String  virtualNodeName=str+"&&"+String.valueOf(i);
                int hash=getHash(virtualNodeName);
                virtualNodes.put(hash,virtualNodeName);
            }
        }
    }


    public static void main(String[] args) {
        String[]  requests={
                "127.0.0.1:1111",
                "221.226.0.1:2222",
                "10.211.0.1:3333",
                "291.226.0.1:2222",
                "100.211.0.1:3333"
        };
        for (int i = 0; i < requests.length; i++) {
            System.out.println("请求 "+i+requests[i]+
                    " ：的哈希值为"+getHash(requests[i])+
                    "，其被路由到的真实节点为"+getServer(requests[i]));
        }
    }

    private  static  String getServer(String request)
    {
        int reqHash=getHash(request);
        SortedMap<Integer,String> subMap = virtualNodes.tailMap(reqHash);
       //返回顺时针找到的第一个虚拟节点
        String virtualNode = subMap.get(subMap.firstKey());
        /*
        通过 virtualNode.substring(0, virtualNode.indexOf("&&"))，
        可以从虚拟节点的名称中提取真实节点的部分，即从开头到 "&&" 之前的部分。
         */
        return  virtualNode.substring(0,virtualNode.indexOf("&&"));
    }


    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值,这里不使用重写hashCode的方法
     */
    public  static int getHash(String server)
    {
        final   int primeNum= 16777619; //设置的常数质数—32位
        int     initialHash= (int) 2166136261L; //初始Hash值—32位
        for (int i = 0; i < server.length(); i++) {
            initialHash=(initialHash^server.charAt(i))*primeNum; //str.charAt(i) 返回的是字符对应的 Unicode 编码值（整数）
            initialHash+=initialHash<<13; //左移13位
            initialHash^=initialHash>>7;
            initialHash+=initialHash<<3;
            initialHash^=initialHash>>17;
            initialHash+=initialHash<<5;
            if (initialHash<0)
            { //如果算出来的值为负数 取其绝对值
                initialHash=Math.abs(initialHash);
            }
        }
        return  initialHash;
    }

}



//不带虚拟节点的一致性哈希
class  ConsistentHashWithoutVirtualNode{

    /*
    待添加入Hash环的服务器列表
     */
    private  static String[] servers={
            "192.168.0.0:111",
            "192.168.0.1:111",
            "192.168.0.2:111",
            "192.168.0.3:111",
            "192.168.0.4:111"
    };

    /*
    * 使用TreeMap来模拟哈希环
    key表示服务器的hash值，value表示服务器的ip+port(名称)
     */
    private  static SortedMap<Integer,String> sortedMap=new TreeMap<Integer,String>();

    //程序初始化时将all servers 放入 哈希环(TreeMap)上
    static {
        for (String server : servers) {
            int hash = getHash(server); //计算每个服务器的hash值
            System.out.println(server+"放入哈希环上，其Hash值为"+hash);
            sortedMap.put(hash, server);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String[] nodes={
                "127.0.0.1:1111",
                "221.226.0.1:2222",
                "192.168.100.1:8822",
                "10.211.0.1:3333"};
        for (int i = 0; i < nodes.length; i++) {
            System.out.println(nodes[i]+"的hash值为"+getHash(nodes[i])+
                    "请求被路由到的Server节点为"+getServer(nodes[i]));
        }
    }

    //外来请求映射到指定Server
    public  static String getServer(String node)
    {
        int nodeHash=getHash(node);
        //根据输入Key找到大于该key的子Map（子红黑树）
        SortedMap<Integer, String> subMap = sortedMap.tailMap(nodeHash);
        return  subMap.get(subMap.firstKey());//返回顺时针查找到的第一个Server节点的ip+port
    }

    //计算每个server的哈希值
    /*
    FNV1_32_HASH（Fowler-Noll-Vo）是一种非常简单但有效的哈希算法，
    用于将输入数据映射为一个32位的哈希值。
    FNV1_32_HASH 算法的核心步骤如下：
     初始化哈希值为一个称为“偏移基准”（offset basis）的常数。
     遍历输入数据的每个字节（或字符）：
     对当前字节的值执行异或操作。
     将结果乘以一个称为“质数”（prime number）的常数。
     最终得到的结果即为哈希值。
     */
    public  static int getHash(String server)
    {
        final   int primeNum= 16777619; //设置的常数质数—32位
        int     initialHash= (int) 2166136261L; //初始Hash值—32位
        for (int i = 0; i < server.length(); i++) {
            initialHash=(initialHash^server.charAt(i))*primeNum; //str.charAt(i) 返回的是字符对应的 Unicode 编码值（整数）
            initialHash+=initialHash<<13; //左移13位
            initialHash^=initialHash>>7;
            initialHash+=initialHash<<3;
            initialHash^=initialHash>>17;
            initialHash+=initialHash<<5;
            if (initialHash<0)
            { //如果算出来的值为负数 取其绝对值
                initialHash=Math.abs(initialHash);
            }
        }
        return  initialHash;
    }



}