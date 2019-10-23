package com.tju.gowl.rank;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class unDirectedGraph {
    public static class Graph {
//        private int MAX_VERTS = 400;//表示顶点的个数
        public static final List<Integer> poolWeightList = new ArrayList<>();//用来存储顶点的数
        public static final Map<Integer, Vertex> vertexMap = new ConcurrentHashMap<>();//用来存储顶点的数
        public static final Map<Integer, List<Integer>> adjList = new ConcurrentHashMap<>();

//        int adjMat[][];//用邻接矩阵来存储边,数组元素0表示没有边界，1表示有边界
        private int nPool;
        private int weightPool;
        private Stack theStack = new Stack<Integer>();//用栈实现深度优先搜索
//        private Queue queue;//用队列实现广度优先搜索
        /**
         * 顶点类
         * @author vae
         */

        class Vertex {
            public int weight;
            public boolean wasVisited;
            public int poolIndex;

            public Vertex(){
                this.weight = 1;
                this.wasVisited = false;
                this.poolIndex = -1;
            }
        }

        public Graph() {
            this.nPool = 0;
        }

        //将顶点添加到数组中，是否访问标志置为wasVisited=false(未访问)
        public void addVertex(int lab) {

            if(vertexMap.containsKey(lab)){
//                System.out.println("node "+lab+" exists!");
                int tmp = vertexMap.get(lab).weight;
                vertexMap.get(lab).weight = tmp + 1;
                return;
            }
            else{
                Vertex v = new Vertex();
                vertexMap.put(lab, v);
            }

        }

        //注意用邻接矩阵表示边，是对称的，两部分都要赋值
        public void addEdge(int start, int end) {
            if(adjList.containsKey(start)){
                adjList.get(start).add(end);
            }
            else {
                adjList.put(start,new ArrayList<>());
                adjList.get(start).add(end);
            }

            if(adjList.containsKey(end)){
                adjList.get(end).add(start);
            }
            else {
                adjList.put(end,new ArrayList<>());
                adjList.get(end).add(start);
            }
        }

        //打印某个顶点表示的值
        public void displayVertex(int v) {
            System.out.println("node"+ v);
        }
        /**深度优先搜索算法:
         * 1、用peek()方法检查栈顶的顶点
         * 2、用getAdjUnvisitedVertex()方法找到当前栈顶点邻接且未被访问的顶点
         * 3、第二步方法返回值不等于-1则找到下一个未访问的邻接顶点，访问这个顶点，并入栈
         *    如果第二步方法返回值等于 -1，则没有找到，出栈
         */
        public void depthFirstSearch() {
            //从第一个顶点开始访问
            Iterator<Map.Entry<Integer, Vertex>> entryIter = vertexMap.entrySet().iterator();
            while(entryIter.hasNext()){
                weightPool = 0;
                Map.Entry<Integer, Vertex> entry = entryIter.next();
                int lab = entry.getKey();
                Vertex vTmp = entry.getValue();
                boolean flagVisit = vTmp.wasVisited;
//                System.out.println("AAA"+flagVisit);
                if(!flagVisit){
                    vTmp.wasVisited = true;
//                    System.out.println("lab"+lab);
                    theStack.push(lab);
                    vTmp.poolIndex = nPool;
                    weightPool = weightPool + vTmp.weight;
//                    displayVertex(lab);
                    while(!theStack.isEmpty()){
//                        System.out.println("AAA");
                        boolean flag = getAdjUnvisitedVertex((Integer) theStack.peek());
                        if(!flag) {   //如果当前顶点值为-1，则表示没有邻接且未被访问顶点，那么出栈顶点
                            theStack.pop();
                        }
                    }
                }
                nPool++;
                poolWeightList.add(weightPool);
            }

        }

        //找到与某一顶点邻接且未被访问的顶点
        public boolean getAdjUnvisitedVertex(Integer v) {
//            List<Integer> tmp = new ArrayList<>();
            boolean flag  = false;
            if(adjList.containsKey(v)){
//                System.out.println("v"+v);
                List<Integer> vList = adjList.get(v);
//                System.out.println("vlist"+vList);

                Iterator<Integer> iter = vList.iterator();
                while(iter.hasNext()){
                    int vTmp = iter.next();
                    Vertex vertexTmp = vertexMap.get(vTmp);
                    if(vertexTmp.wasVisited ==  false){
                        theStack.push(vTmp);
                        vertexTmp.poolIndex = nPool;
                        weightPool = weightPool + vertexTmp.weight;
//                        displayVertex(vTmp);
                        vertexTmp.wasVisited = true;
                        flag = true;
                    }
                }
            }
            return flag;
        }

        public static int getPropertyWeight(int equiPro1) {
            if(vertexMap.containsKey(equiPro1)){
                int poolIndexTmp = vertexMap.get(equiPro1).poolIndex;
                int weightImp = poolWeightList.get(poolIndexTmp);
                return weightImp;
            }
            return 0;
        }
        public static void main(String[] args) {
            Graph graph = new Graph();
            graph.addVertex(1);
            graph.addVertex(1);
            graph.addVertex(1);
            graph.addVertex(1);
            graph.addVertex(1);
            graph.addVertex(2);
            graph.addVertex(3);
            graph.addVertex(5);
            graph.addVertex(6);

            graph.addEdge(1, 5);//AB
            graph.addEdge(6, 5);//BC
//            graph.addEdge(1, 3);//AD
//            graph.addEdge(2, 3);//DE

            System.out.println("深度优先搜索算法 :");
            graph.depthFirstSearch();//ABCDE

            System.out.println();
            Iterator<Map.Entry<Integer, Vertex>> entryIter = vertexMap.entrySet().iterator();
            while(entryIter.hasNext()) {
                Map.Entry<Integer, Vertex> entry = entryIter.next();
                int lab = entry.getKey();
                Vertex vTmp = entry.getValue();
                System.out.println("lab "+lab+"  "+vTmp.poolIndex);
            }
            System.out.println(poolWeightList);
        }
    }
}
