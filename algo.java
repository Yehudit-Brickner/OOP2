

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.PrintWriter;
import java.util.*;


public class algo implements DirectedWeightedGraphAlgorithms {
    public Graph myGraph; //the graph that we are working on.


    //this algo constructor gets a json file and makes a new graph from it.
    public algo(String jsonName) {
        try {
            myGraph = new Graph(jsonName);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // this algo constructor copies a graph
    public algo(Graph gr1) {
        this.myGraph = gr1;
    }


    //default constructor
    public algo() {
        this.myGraph = new Graph();
    }


    /**
     * makes a copy of the given graph into are graph-myGraph
     * trun time O(|v|+|e|) v=vertexes, e=edges .
     *
     * @param g
     */

    @Override
    public void init(DirectedWeightedGraph g) {

        Iterator<NodeData> al = g.nodeIter();//making iterator for g.
        while (al.hasNext()) {
            NodeData curr = al.next();
            this.myGraph.addNode(curr);//adding to my graph node by node
        }
        Iterator<EdgeData> ag = g.edgeIter();
        while (ag.hasNext()) {
            EdgeData curr = ag.next();
            this.myGraph.connect(curr.getSrc(), curr.getDest(), curr.getWeight()); //adding to myGraph edge by edge.
        }

    }

    /**
     * returns the graph
     * the run time is O(1)
     *
     * @return
     */

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.myGraph;
    }

    /**
     * this creates a deep copy of the graph
     * run time O(|v|+|e|) v=vertexes, e=edges .
     *
     * @return
     */

    @Override
    public DirectedWeightedGraph copy() {
        Graph g = new Graph();
        Iterator<NodeData> a = myGraph.nodeIter();
        while (a.hasNext()) {
            g.addNode(a.next());
        }
        Iterator<EdgeData> b = myGraph.edgeIter();
        while (b.hasNext()) {
            EdgeData e = b.next();
            g.connect(e.getSrc(), e.getDest(), e.getWeight());
        }
        return g;
    }


    /**
     * we will check if you can get from every Vertex to Every Vertex
     * if the graph has no nodes by default it is connected
     * if the graph has fewer edges than vertexes it can not be connected
     * if neither of the if give us an answer
     * we will start by marking each vertex as not seen
     * we will create a queue and add the first vertex to it
     * while the queue isnt empty
     * we will mark the vertex as seen and iterate through the vertexes edges and add the edges dest to the queue
     * then we will poll the vertex.
     * after we will iterate through the Vertexes and make sure that all the Vertexes have been seen
     * if we find a vertex that hasn't been seen the graph isn't connected
     * if all the vertexes have been seen we will flip the graph
     * and repeat on the flipped graph
     *
     * the running time is O(|v|+|e|) v=vertex
     *
     * @return
     */

    @Override
    public boolean isConnected() {
        if (this.myGraph.nodeSize() == 0) {
            return true;
        }
        if (this.myGraph.edgeSize() < this.myGraph.nodeSize()) {
            return false;
        }
// set tag to 0
        Iterator<NodeData> a1 = myGraph.nodeIter();
        while (a1.hasNext()) {
            a1.next().setTag(0);
        }

        Iterator<NodeData> a = myGraph.nodeIter();
        Vertex nodi = null;
        Deque<Vertex> q = new LinkedList<Vertex>();
        if (a.hasNext()) {
            nodi = (Vertex) a.next();
            q.add(nodi);
        }
        while (q.size() > 0) {
            Vertex v = q.poll();
            v.setTag(1);
            Iterator<EdgeData> it = myGraph.edgeIter(v.getKey());
            while (it.hasNext()) {
                Edge e = (Edge) it.next();
                Vertex ver = myGraph.Nodes.get(e.getDest());
                if (ver.tag != 1) {
                    if (!q.contains(ver)) {
                        q.add(ver);
                    }
                }
            }
        }


//        if(a.hasNext()) {
//            nodi = (Vertex) a.next();
//            if (nodi.getTag()!=1){
//                tagchild(nodi,myGraph,0);
//            }
//        }

        //check if tagged
        Iterator<NodeData> b = myGraph.nodeIter();
        while (b.hasNext()) {
            if (b.next().getTag() != 1)
                return false;
        }


        Graph newG = flipGraph(myGraph);


        Iterator<NodeData> c = newG.nodeIter();
        while (c.hasNext()) {
            c.next().setTag(0);
        }
        Iterator<NodeData> d = newG.nodeIter();
        // Vertex nody=null;
        Deque<Vertex> q2 = new LinkedList<Vertex>();
        if (d.hasNext()) {
            nodi = (Vertex) d.next();
            q2.add(nodi);
        }
        while (q2.size() > 0) {
            Vertex v = q2.poll();
            v.setTag(1);
            Iterator<EdgeData> it = newG.edgeIter(v.getKey());
            while (it.hasNext()) {
                Edge e = (Edge) it.next();
                Vertex ver = newG.Nodes.get(e.getDest());
                if (ver.tag != 1) {
                    if (!q2.contains(ver)) {
                        q2.add(ver);
                    }
                }
            }
        }

//        if(d.hasNext()) {
//            nody = (Vertex)d.next();
//            if(nody.getTag()!=1){
//                tagchild(nody,newG,0);
//            }
//        }

        Iterator<NodeData> e = newG.nodeIter();
        while (e.hasNext()) {
            if (e.next().getTag() != 1)
                return false;
        }
        return true;

    }


    /**
     * we will check if the vertx has been seen
     * if it hasn't been seen we will mark that it has been seen
     * we will iterate through the Vertexes in the graph
     * if the vertex we get to while iterating hasn't been seen and isn't vertex v
     * we will see if there is an edge between v and this vertex
     * if there is we will run the function again with the new vertex
     * the run time of this function is O(|v|^2) v=vertex
     *
     * @param v Vertex
     * @param g Graph
     */

    public void tagchild(Vertex v, Graph g, int count) {
        if (count < 1000) {
            count = count + 1;
            if (v.getTag() != 1) {
                v.setTag(1);
                Iterator<NodeData> it = g.nodeIter();
                while (it.hasNext()) {
                    Vertex n = (Vertex) it.next();
                    if (n.getTag() != 1 && n != v) {
                        if (g.edg.get(v.getKey() + "," + n.getKey()) != null) {
                            System.out.println(v.getKey());
                            tagchild(n, g, count);
                        }
                    }
                }
            }
        }
    }


    /**
     * we will start by creating a new graph
     * we will copy the vertexes from the given graph to our new graph
     * we will copy the edges from the given graph to our new graph
     * BUT we will switch the src and dest
     * run time O(|v|+|e|) v=vertexes, e=edges
     *
     * @param myGraph
     * @return
     */

    public Graph flipGraph(Graph myGraph) {
        Graph newGraph = new Graph();

        Iterator<NodeData> a = myGraph.nodeIter();
        while (a.hasNext()) {
            Vertex n1 = (Vertex) a.next();
            //  n1.nodes.clear();
            // n1.setTag(0);
            Vertex n2 = new Vertex(n1.getKey(), (Geo) n1.getLocation());
            newGraph.addNode(n2);
        }
        Iterator<EdgeData> b = myGraph.edgeIter();
        while (b.hasNext()) {
            Edge e1 = (Edge) b.next();
            Vertex n1 = this.myGraph.Nodes.get(e1.src);
            Vertex n2 = this.myGraph.Nodes.get(e1.dis);
            newGraph.connect(n2.getKey(), n1.getKey(), e1.getWeight());
        }
        return newGraph;
    }


    /**
     * create  a pair from the dijksta function
     * create a hashmap that gets the hashmap of the distances from pair
     * create a list l1 with the src and dest keys
     * create the list l2 by getting the value of l1 from the hashmap
     * return l2
     * the run time of the function is O((|v|+e|)log|e|) v=vertex e=edge because of dijsktra
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */

    @Override
    public double shortestPathDist(int src, int dest) {

//        HashMap<List<Integer>, Double> not_mat = Floyd_Warshall(this.myGraph);
//        List<Integer> l1 = new ArrayList<Integer>();
//        l1.add(src);
//        l1.add(dest);
//        double ans = not_mat.get(l1);
//        return ans;
        Pair p = dijkstaList(src);
        HashMap<List<Integer>, Double> hash = p.hash_d;
        List<Integer> l = new ArrayList<Integer>();
        l.add(src);
        l.add(dest);
        double ans = hash.get(l);
        return ans;

    }


    /**
     * create  a pair from the dijksta function
     * create a hashmap that gets the hashmap of the lists from pair
     * create a list l1 with the src and dest keys
     * create a new list  l2 from access the hashmap with the l1 and get its value
     * create a new list  l3 of Vertexes from the l2 we got from the hashmap
     * return l3
     * the run time of the function is O((|v|+e|)log|e|) v=vertex e=edge because of dijsktra
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
//        HashMap<List<Integer>,Double> s=Floyd_Warshall(this.myGraph);
//        List<Integer> l1 = new ArrayList<Integer>();
//        l1.add(src);
//        l1.add(dest);
//        if(s.get(l1)==0.0 || s.get(l1)==Double.MAX_VALUE){
//            return null;
//        }
//        HashMap<List<Integer>, List<Integer>> hash = Floyd_Warshall_list(this.myGraph);
//
//        List<Integer> ans1 = hash.get(l1);
//        List<NodeData> ans=new ArrayList<NodeData>();
//        for (int i=0; i<ans1.size();i++){
//          ans.add( myGraph.Nodes.get(ans1.get(i)));
//        }
//        return ans;

        // List<Integer> hash=dijkstaList(src,dest);
        Pair p = dijkstaList(src);
        HashMap<List<Integer>, List<Integer>> hash = p.hash_l;
        List<Integer> l = new ArrayList<Integer>();
        l.add(src);
        l.add(dest);
        List ansList = hash.get(l);
        List<NodeData> ans = new ArrayList<NodeData>();
        for (int i = 0; i < ansList.size(); i++) {
            ans.add(myGraph.Nodes.get(ansList.get(i)));
        }
        return ans;

    }


    /**
     * if the graph isn't connected the graph doesn't have a center
     * loop through the vertexes and run dijkstra.
     * find the farthest Vertex from the src add it to the hashmap af farthest vertexes
     * we will loop through the hashmap to find the vertex with the smallest value
     * and return that vertex
     * the run time of the function is  O((|v^2|+e|)log|e|) v=vertex e=edge
     *
     * @return
     */

    @Override
    public NodeData center() {
        if (isConnected() == false) {
            return null;
        }
//        HashMap<List<Integer>, Double> shortlen = Floyd_Warshall(this.myGraph);
//        HashMap<Integer, Double> al = new HashMap<Integer, Double>();
//
//        Iterator<NodeData> it1 = myGraph.nodeIter();
//        while (it1.hasNext()) {
//            double big = 0.0;
//            Vertex v1 = (Vertex) it1.next();
//            Iterator<NodeData> it2 = myGraph.nodeIter();
//            while (it2.hasNext()) {
//                Vertex v2 = (Vertex) it2.next();
//                List<Integer> l1 = new ArrayList<Integer>();
//                l1.add(v1.getKey());
//                l1.add(v2.getKey());
//                if (shortlen.get(l1) > big) {
//                    big = shortlen.get(l1);
//                }
//            }
//            al.put(v1.getKey(), big);
//        }
//
//        double small = Double.MAX_VALUE;
//        int key = 0;
//        Iterator<NodeData> it3 = myGraph.nodeIter();
//        while (it3.hasNext()) {
//            Vertex v3 = (Vertex) it3.next();
//            if (al.get(v3.getKey()) < small) {
//                key = v3.getKey();
//                small = al.get(key);
//            }
//        }
//        Vertex ans = myGraph.Nodes.get(key);
//        return ans;
//

        HashMap<Integer, Double> al = new HashMap<Integer, Double>();
        Iterator<NodeData> it1 = myGraph.nodeIter();
        while(it1.hasNext()){
        Vertex v1=(Vertex) it1.next();
        Pair p=dijkstaList(v1.getKey());
        double big=0;
        HashMap<List<Integer>,Double> hash=p.getHash_d();
           for(Double d: hash.values()){
            if(d>big) {
                big = d;
            }
           }
        al.put(v1.getKey(),big);
        }
        double small=Double.MAX_VALUE;
        int ansI=-1;
        for (Integer i: al.keySet()) {
            if(al.get(i)<small){
                small=al.get(i);
                ansI=i;
            }
        }
        Vertex ans=(Vertex)myGraph.Nodes.get(ansI);
        return ans;
    }


    /**
     * if cities is empty return null
     * if cities has 1 city return cities
     * hashmap1= distance between Vertexes
     * hashmap2= list of vertexes
     * loop through the vertexes in cities and run dijextra on all of them and add the values,
     * to the respective hashmaps
     * create 2 arraylists of the cities city1,city2
     * crete a loop on a loop to find the shortest path in the cities list
     * create list ans and add the Vertexes in that path to it
     * go through ans and remove from city 2 the vertexes in ans
     * while city2 >0
     * go through city2 and find the smallest path that we can add on to the left or right
     * add the Vertexes in that path to ans
     * go through ans and remove from city 2 the vertexes in ans
     * we have added all the vertexes from cities to the ans
     * now we need to connect the last Vertex to the first vertex
     * return ans
     * if at any time a list when we are searching for a path and cant find any we can say that there are no pathes.
     * the run time of the function is  O((|v^2|+e|)log|e|)  or the amount of vertexes in cities! v=vertex e=edge
     *
     * @param cities
     * @return
     */




    @Override
    public List<NodeData> tsp(List<NodeData> cities) {

        if (cities.isEmpty()) {
            return null;
        }
        if (cities.size() == 1) {
            return cities;
        }
//        //HashMap<List<Integer>,Double> mat_d =Floyd_Warshall(this.myGraph);
//        //HashMap<List<Integer>,List<Integer>> mat_n =Floyd_Warshall_list(this.myGraph);
//        Pair p = Floyd_Warshall_list1(this.myGraph);
//        HashMap<List<Integer>, Double> mat_d = p.getHash_d();
//        HashMap<List<Integer>, List<Integer>> mat_n = p.getHash_l();
//        List<NodeData> city1 = new ArrayList<NodeData>();
//        List<NodeData> city2 = new ArrayList<NodeData>();
//        for (int i = 0; i < cities.size(); i++) {
//            city1.add(cities.get(i));
//            city2.add(cities.get(i));
//        }
//        double small = Double.MAX_VALUE;
//        List<Integer> l2 = new ArrayList<Integer>();
//        //find the smallest connector in this group;
//
//        for (int i = 0; i < city1.size(); i++) {
//            for (int j = 0; j < city1.size(); j++) {
//                if (i != j) {
//                    int v1 = city1.get(i).getKey();
//                    int v2 = city1.get(j).getKey();
//
//                    List<Integer> l1 = new ArrayList<Integer>();
//                    l1.add(v1);
//                    l1.add(v2);
//                    if (small > mat_d.get(l1) && mat_d.get(l1) != 0) {
//                        small = mat_d.get(l1);
//                        l2 = l1;
//                    }
//                }
//            }
//        }
//        ArrayList<Integer> ans1 = new ArrayList<Integer>();
//        List<Integer> l1 = new ArrayList<Integer>();
//        l1 = mat_n.get(l2);
//        //adding the nodes in l2 path ans
//        for (int i = 0; i < l1.size(); i++) {
//            ans1.add(l1.get(i));
//        }
//
//        //make city smaller-by getting rid of the nodes we just added
//        for (int j = 0; j < l1.size(); j++) {
//            int k1 = -1;
//            for (int i = 0; i < city2.size(); i++) {
//                if (city2.get(i).getKey() == l1.get(j)) {
//                    k1 = i;
//                }
//            }
//            if (k1 != -1) {
//                city2.remove(k1);
//            }
//        }
//
//        while (city2.size() > 0) {
//
//            double small2 = Double.MAX_VALUE;
//
//            int key_start = ans1.get(0);
//            int key_end = ans1.get(ans1.size() - 1);
//            boolean s = false;
//            boolean e = false;
//
//            List<Integer> l4 = new ArrayList<Integer>();
//            for (int i = 0; i < city2.size(); i++) {
//                if (city2.get(i).getKey() != key_start) {
//                    List<Integer> l3 = new ArrayList<Integer>();
//                    l3.add(city2.get(i).getKey());
//                    l3.add(key_start);
//                    if (mat_d.get(l3) < small2) {
//                        l4 = l3;
//                        s = true;
//                    }
//                }
//            }
//            for (int i = 0; i < city2.size(); i++) {
//                if (city2.get(i).getKey() != key_end) {
//                    List<Integer> l3 = new ArrayList<Integer>();
//                    l3.add(key_end);
//                    l3.add(city2.get(i).getKey());
//                    if (mat_d.get(l3) < small2) {
//                        l4 = l3;
//                        e = true;
//                        s = false;
//                    }
//                }
//            }
//
//            l1 = mat_n.get(l4);
//            //adding the nodes in l2 path ans
//            if (s) {
//                for (int i = l1.size() - 2; i > 0 - 1; i--) { // dont add last its already in the list
//                    ans1.add(0, l1.get(i));
//                }
//            }
//            if (e) {
//                for (int i = 1; i < l1.size(); i++) { // dont add first its already in the list
//                    ans1.add(l1.get(i));
//                }
//            }
//            for (int j = 0; j < l1.size(); j++) {
//                int k1 = -1;
//                for (int i = 0; i < city2.size(); i++) {
//                    if (city2.get(i).getKey() == l1.get(j)) {
//                        k1 = i;
//                    }
//                }
//                if (k1 != -1) {
//                    city2.remove(k1);
//                }
//            }
//        }
//
//        ArrayList<Integer> l5 = new ArrayList<Integer>();
//        l5.add(ans1.get(ans1.size() - 1));
//        l5.add(ans1.get(0));
//        List<Integer> l6 = mat_n.get(l5);
//        for (int i = 1; i < l6.size(); i++) {
//            ans1.add(l6.get(i));
//        }
//        List<NodeData> ans = new ArrayList<NodeData>();
//        for (int i = 0; i < ans1.size(); i++) {
//            ans.add(myGraph.Nodes.get(ans1.get(i)));
//        }
//        return ans;

        HashMap<List<Integer>,Double> all_d=new HashMap<List<Integer>,Double>();
        HashMap<List<Integer>,List<Integer>> all_l =new HashMap<List<Integer>,List<Integer>>();
        System.out.println(cities.size());
        for (int i= 0; i<cities.size();i++){
            int key=cities.get(i).getKey();
            Pair p=dijkstaList(key);
            HashMap<List<Integer>,Double> hashd= p.getHash_d();
            HashMap<List<Integer>,List<Integer>> hashl= p.getHash_l();
            for (List<Integer> l: hashd.keySet()) {
                all_d.put(l,hashd.get(l));
            }
            for (List<Integer> l: hashl.keySet()) {
                all_l.put(l,hashl.get(l));
            }
        }

        List<NodeData> city1 = new ArrayList<NodeData>();
        List<NodeData> city2 = new ArrayList<NodeData>();
        for (int i = 0; i < cities.size(); i++) {
            city1.add(cities.get(i));
            city2.add(cities.get(i));
        }


        double small = Double.MAX_VALUE;
        List<Integer> l2 = new ArrayList<Integer>();
        //find the smallest connector in this group;

        for (int i = 0; i < city1.size(); i++) {
            for (int j = 0; j < city1.size(); j++) {
                if (i != j) {
                    int v1 = city1.get(i).getKey();
                    int v2 = city1.get(j).getKey();

                    List<Integer> l1 = new ArrayList<Integer>();
                    l1.add(v1);
                    l1.add(v2);
                    if (small > all_d.get(l1) && all_d.get(l1) != 0) {
                        small = all_d.get(l1);
                        l2 = l1; // the smallest list
                    }
                }
            }
        }
        if(l2.size()==0){
            List<NodeData> l1=new ArrayList<NodeData>();
            return l1;
        }

        List<Integer> ans1= new ArrayList<Integer>(); // adding the Vertexs ids to the path
        for (int i=0;i<all_l.get(l2).size();i++){
            ans1.add(all_l.get(l2).get(i));
        }

       // make city smaller-by getting rid of the nodes we just added
        for (int j = 0; j < l2.size(); j++) {
            int k1 = -1;
            for (int i = 0; i < city2.size(); i++) {
                if (city2.get(i).getKey() == l2.get(j)) {
                    k1 = i;
                }
            }
            if (k1 != -1) {
                city2.remove(k1);
            }
        }

        while (city2.size() > 0) {

            double small2 = Double.MAX_VALUE;

            int key_start = ans1.get(0);
            int key_end = ans1.get(ans1.size() - 1);
            boolean s = false;
            boolean e = false;

            List<Integer> l4 = new ArrayList<Integer>();
            for (int i = 0; i < city2.size(); i++) {
                if (city2.get(i).getKey() != key_start) {
                    List<Integer> l3 = new ArrayList<Integer>();
                    l3.add(city2.get(i).getKey());
                    l3.add(key_start);
                    if (all_d.get(l3) < small2) {
                        l4 = l3;
                        s = true;
                    }
                }
            }
            for (int i = 0; i < city2.size(); i++) {
                if (city2.get(i).getKey() != key_end) {
                    List<Integer> l3 = new ArrayList<Integer>();
                    l3.add(key_end);
                    l3.add(city2.get(i).getKey());
                    if (all_d.get(l3) < small2) {
                        l4 = l3;
                        e = true;
                        s = false;
                    }
                }
            }
if(!e && ! s){
    List<NodeData> l1=new ArrayList<NodeData>();
    return l1;
}
            List<Integer> l1 = all_l.get(l4);
            //adding the nodes in l2 path ans
            if (s) {
                for (int i = l1.size() - 2; i > 0 - 1; i--) { // dont add last if its already in the list
                    ans1.add(0, l1.get(i));
                }
            }
            if (e) {
                for (int i = 1; i < l1.size(); i++) { // dont add first if  its already in the list
                    ans1.add(l1.get(i));
                }
            }
            if (l1!=null) {
                for (int j = 0; j < l1.size(); j++) {
                    int k1 = -1;
                    for (int i = 0; i < city2.size(); i++) {
                        if (city2.get(i).getKey() == l1.get(j)) {
                            k1 = i;
                        }
                    }
                    if (k1 != -1) {
                        city2.remove(k1);
                    }
                }
            }
        }

        ArrayList<Integer> l5 = new ArrayList<Integer>();
        l5.add(ans1.get(ans1.size() - 1));
        l5.add(ans1.get(0));
        List<Integer> l6 = all_l.get(l5);
        for (int i = 1; i < l6.size(); i++) {
            ans1.add(l6.get(i));
        }


List<NodeData> ans=new ArrayList<NodeData>();
        for (int i=0;i<ans1.size();i++){
            ans.add(myGraph.Nodes.get(ans1.get(i)));
        }
        return ans;
    }


    /**
     * we created a map
     * than a jsonObject and jsonArray
     * we will iterate through the edges and create a linkedList in every iterartion.
     * we will add the edges parts to the linkedList and dd the linked list to the json array
     * when we finish iterating through the edges we will add the jsonArray to the Json object.
     * and do the same thing for the Vertexes.
     * than we will open a file writer and write the json object into a file
     * we will flush and clos the file
     * if we didnt have ant errors we will return true else we will false
     * the run time is O(|v|+|e|) v=vertexes, e=edges .
     *
     * @param file - the file name (may include a relative path).
     * @return
     */

    @Override
    public boolean save(String file) {
        try {
            Map lod;
            JSONObject najeeb = new JSONObject();
            JSONArray yehudit = new JSONArray();
            Iterator<EdgeData> iter = this.myGraph.edgeIter();
            while (iter.hasNext()) {
                lod = new LinkedHashMap(3);
                EdgeData ed = iter.next();
                lod.put("src", ed.getSrc());
                lod.put("w", ed.getWeight());
                lod.put("dest", ed.getDest());
                yehudit.add(lod);
            }
            najeeb.put("Edges", yehudit);
            yehudit = new JSONArray();
            Iterator<NodeData> iter2 = this.myGraph.nodeIter();
            while (iter2.hasNext()) {
                lod = new LinkedHashMap(2);
                NodeData nod = iter2.next();
                lod.put("pos", nod.getLocation().x() + "," + nod.getLocation().y() + "," + nod.getLocation().z());
                lod.put("id", nod.getKey());
                yehudit.add(lod);
            }
            najeeb.put("Nodes", yehudit);
            PrintWriter pro = new PrintWriter(file);
            pro.write(najeeb.toString());
            pro.flush();
            pro.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * this function loads a graph from json file
     * the run time is O(|v|+|e|) v=vertexes, e=edges .
     *
     * @param file - file name of JSON file
     * @return
     */

    @Override
    public boolean load(String file) {

        try {
            DirectedWeightedGraph newGraph = new Graph(file);
            myGraph = (Graph) newGraph;

        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * this function finds the shortest path between all 2 vertexes
     * here is a explanation to how the code works
     * https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm
     * the run time of the function is O(|v|^3)
     *
     * @param g
     * @return
     */

    public HashMap<List<Integer>, Double> Floyd_Warshall(Graph g) {

        HashMap<List<Integer>, Double> mat_rep = new HashMap<List<Integer>, Double>();
        Vertex n1 = null;
        Vertex n2 = null;
        Vertex n3 = null;
        //filling in the initial values in the hashmap
        Iterator<NodeData> it1 = myGraph.nodeIter();
        while (it1.hasNext()) {
            n1 = (Vertex) it1.next();
            Iterator<NodeData> it2 = myGraph.nodeIter();
            while (it2.hasNext()) {
                n2 = (Vertex) it2.next();
                List<Integer> l1 = new ArrayList<Integer>();
                l1.add(n1.getKey());
                l1.add(n2.getKey());
                if (this.myGraph.edg.get(n1.getKey() + "," + n2.getKey()) != null) {
                    mat_rep.put(l1, this.myGraph.edg.get(n1.getKey() + "," + n2.getKey()).getWeight());
                } else {
                    if (l1.get(0) == l1.get(1)) {
                        mat_rep.put(l1, 0.0);
                    } else {
                        mat_rep.put(l1, Double.MAX_VALUE);
                    }
                }
            }
        }

        double w1 = 0;
        double w2 = 0;
        double w3 = 0;

        Iterator<NodeData> iter1 = myGraph.nodeIter();  //k
        while (iter1.hasNext()) {
            n1 = (Vertex) iter1.next();
            Iterator<NodeData> iter2 = myGraph.nodeIter();  //i
            while (iter2.hasNext()) {
                n2 = (Vertex) iter2.next();
                if (n1.getKey() != n2.getKey()) { // dont want the same ones
                    Iterator<NodeData> iter3 = myGraph.nodeIter();  //j
                    while (iter3.hasNext()) {
                        n3 = (Vertex) iter3.next();
                        if (n3.getKey() != n1.getKey() && n3.getKey() != n2.getKey()) { // dont want the same ones
                            List<Integer> l1 = new ArrayList<Integer>(); // l1= ij
                            l1.add(n2.getKey());
                            l1.add(n3.getKey());

                            List<Integer> l2 = new ArrayList<Integer>(); // l2=ik
                            l2.add(n2.getKey());
                            l2.add(n1.getKey());

                            List<Integer> l3 = new ArrayList<Integer>(); // l3 = kj
                            l3.add(n1.getKey());
                            l3.add(n3.getKey());

                            w1 = mat_rep.get(l1);

                            w2 = mat_rep.get(l2);

                            w3 = mat_rep.get(l3);

                            if (w1 > w2 + w3) {
                                mat_rep.put(l1, w2 + w3);

                            }
                        }
                    }
                }
            }
        }
        return mat_rep;
    }


    /**
     * this function finds the shortest path between all 2 vertexes with a list of vertexes keys in the path
     * here is a explanation to how the Floyd_Warshall works
     * https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm
     * in this function as we updated the main hashmap we update the other hashmap with the path
     * the run time of the function is O(|v|^3)
     *
     * @param g
     * @return
     */

    public HashMap<List<Integer>, List<Integer>> Floyd_Warshall_list(Graph g) {
        // public Pair Floyd_Warshall_list (Graph g){
        HashMap<List<Integer>, Double> mat_rep = new HashMap<List<Integer>, Double>();
        HashMap<List<Integer>, List<Integer>> mat = new HashMap<List<Integer>, List<Integer>>();
        //filling in the initial values in the hashmap
        Iterator<NodeData> it1 = g.nodeIter();
        while (it1.hasNext()) {
            Vertex n1 = (Vertex) it1.next();
            Iterator<NodeData> it2 = g.nodeIter();
            while (it2.hasNext()) {
                Vertex n2 = (Vertex) it2.next();
                List<Integer> l1 = new ArrayList<Integer>();
                l1.add(n1.getKey());
                l1.add(n2.getKey());
                if (g.edg.get(n1.getKey() + "," + n2.getKey()) != null) {
                    mat_rep.put(l1, g.edg.get(n1.getKey() + "," + n2.getKey()).getWeight());
                    // List<> l2=new ArrayList<NodeData>();
                    List<Integer> l2 = new ArrayList<Integer>();
                    l2.add(n1.getKey());
                    l2.add(n2.getKey());
                    mat.put(l1, l2);
                } else {
                    if (l1.get(0) == l1.get(1)) {
                        mat_rep.put(l1, 0.0);
                        List<Integer> l2 = new ArrayList<Integer>();
                        l2.add(n1.getKey());
                        mat.put(l1, l2);
                    } else {
                        mat_rep.put(l1, Double.MAX_VALUE);
                        List<Integer> l2 = new ArrayList<Integer>();
                        l2.add(n1.getKey());
                        l2.add(n2.getKey());
                        mat.put(l1, l2);
                    }
                }
            }
        }

        double w1 = 0;
        double w2 = 0;
        double w3 = 0;

        Iterator<NodeData> iter1 = g.nodeIter();  //k
        while (iter1.hasNext()) {
            Vertex n1 = (Vertex) iter1.next();
            Iterator<NodeData> iter2 = g.nodeIter();  //i
            while (iter2.hasNext()) {
                Vertex n2 = (Vertex) iter2.next();
                if (n1.getKey() != n2.getKey()) { // dont want the same ones
                    Iterator<NodeData> iter3 = g.nodeIter();  //j
                    while (iter3.hasNext()) {
                        Vertex n3 = (Vertex) iter3.next();
                        if (n3.getKey() != n1.getKey() && n3.getKey() != n2.getKey()) { // dont want the same ones
                            List<Integer> l1 = new ArrayList<Integer>(); // l1= ij
                            l1.add(n2.getKey());
                            l1.add(n3.getKey());

                            List<Integer> l2 = new ArrayList<Integer>(); // l2=ik
                            l2.add(n2.getKey());
                            l2.add(n1.getKey());

                            List<Integer> l3 = new ArrayList<Integer>(); // l3 = kj
                            l3.add(n1.getKey());
                            l3.add(n3.getKey());

                            w1 = mat_rep.get(l1);

                            w2 = mat_rep.get(l2);

                            w3 = mat_rep.get(l3);

                            if (w1 > w2 + w3) {
                                mat_rep.put(l1, w2 + w3);
                                // creating the new list with the path
                                List<Integer> Vs_new = new ArrayList<Integer>();
                                List<Integer> Vs_old1 = new ArrayList<Integer>();
                                List<Integer> Vs_old2 = new ArrayList<Integer>();
                                Vs_old1 = mat.get(l2);
                                for (int i = 0; i < Vs_old1.size(); i++) { // don't want to include last so that we don't have a repeat;
                                    Vs_new.add(Vs_old1.get(i));
                                }
                                // Vs_old.clear();
                                boolean seen = false;
                                Vs_old2 = mat.get(l3);
                                for (int i = 0; i < Vs_old2.size(); i++) {
                                    seen = false;
                                    for (int j = 0; j < Vs_new.size() && !seen; j++) {
                                        if (Vs_new.get(j) == Vs_old2.get(i)) {
                                            seen = true;
                                        }
                                    }
                                    if (!seen)
                                        Vs_new.add(Vs_old2.get(i));
                                }
                                mat.put(l1, Vs_new);

                            }
                        }
                    }
                }
            }

        }
        // Pair p=new Pair(mat_rep,mat);
        return mat;
        //return p;
    }


    // public HashMap<List<Integer>, List<Integer>>Floyd_Warshall_list (Graph g){
    public Pair Floyd_Warshall_list1(Graph g) {
        HashMap<List<Integer>, Double> mat_rep = new HashMap<List<Integer>, Double>();
        HashMap<List<Integer>, List<Integer>> mat = new HashMap<List<Integer>, List<Integer>>();
        //filling in the initial values in the hashmap
        Iterator<NodeData> it1 = g.nodeIter();
        while (it1.hasNext()) {
            Vertex n1 = (Vertex) it1.next();
            Iterator<NodeData> it2 = g.nodeIter();
            while (it2.hasNext()) {
                Vertex n2 = (Vertex) it2.next();
                List<Integer> l1 = new ArrayList<Integer>();
                l1.add(n1.getKey());
                l1.add(n2.getKey());
                if (g.edg.get(n1.getKey() + "," + n2.getKey()) != null) {
                    mat_rep.put(l1, g.edg.get(n1.getKey() + "," + n2.getKey()).getWeight());
                    // List<> l2=new ArrayList<NodeData>();
                    List<Integer> l2 = new ArrayList<Integer>();
                    l2.add(n1.getKey());
                    l2.add(n2.getKey());
                    mat.put(l1, l2);
                } else {
                    if (l1.get(0) == l1.get(1)) {
                        mat_rep.put(l1, 0.0);
                        List<Integer> l2 = new ArrayList<Integer>();
                        l2.add(n1.getKey());
                        mat.put(l1, l2);
                    } else {
                        mat_rep.put(l1, Double.MAX_VALUE);
                        List<Integer> l2 = new ArrayList<Integer>();
                        l2.add(n1.getKey());
                        l2.add(n2.getKey());
                        mat.put(l1, l2);
                    }
                }
            }
        }

        double w1 = 0;
        double w2 = 0;
        double w3 = 0;

        Iterator<NodeData> iter1 = g.nodeIter();  //k
        while (iter1.hasNext()) {
            Vertex n1 = (Vertex) iter1.next();
            Iterator<NodeData> iter2 = g.nodeIter();  //i
            while (iter2.hasNext()) {
                Vertex n2 = (Vertex) iter2.next();
                if (n1.getKey() != n2.getKey()) { // dont want the same ones
                    Iterator<NodeData> iter3 = g.nodeIter();  //j
                    while (iter3.hasNext()) {
                        Vertex n3 = (Vertex) iter3.next();
                        if (n3.getKey() != n1.getKey() && n3.getKey() != n2.getKey()) { // dont want the same ones
                            List<Integer> l1 = new ArrayList<Integer>(); // l1= ij
                            l1.add(n2.getKey());
                            l1.add(n3.getKey());

                            List<Integer> l2 = new ArrayList<Integer>(); // l2=ik
                            l2.add(n2.getKey());
                            l2.add(n1.getKey());

                            List<Integer> l3 = new ArrayList<Integer>(); // l3 = kj
                            l3.add(n1.getKey());
                            l3.add(n3.getKey());

                            w1 = mat_rep.get(l1);

                            w2 = mat_rep.get(l2);

                            w3 = mat_rep.get(l3);

                            if (w1 > w2 + w3) {
                                mat_rep.put(l1, w2 + w3);
                                // creating the new list with the path
                                List<Integer> Vs_new = new ArrayList<Integer>();
                                List<Integer> Vs_old1 = new ArrayList<Integer>();
                                List<Integer> Vs_old2 = new ArrayList<Integer>();
                                Vs_old1 = mat.get(l2);
                                for (int i = 0; i < Vs_old1.size(); i++) { // don't want to include last so that we don't have a repeat;
                                    Vs_new.add(Vs_old1.get(i));
                                }
                                // Vs_old.clear();
                                boolean seen = false;
                                Vs_old2 = mat.get(l3);
                                for (int i = 0; i < Vs_old2.size(); i++) {
                                    seen = false;
                                    for (int j = 0; j < Vs_new.size() && !seen; j++) {
                                        if (Vs_new.get(j) == Vs_old2.get(i)) {
                                            seen = true;
                                        }
                                    }
                                    if (!seen)
                                        Vs_new.add(Vs_old2.get(i));
                                }
                                mat.put(l1, Vs_new);

                            }
                        }
                    }
                }
            }

        }
        Pair p = new Pair(mat_rep, mat);
        //return mat;
        return p;
    }

/*
    public double dijksta(int src, int dest) {
        Comparator<IntDub> ComparatorDouble = new Comparator<IntDub>() {
            @Override
            public int compare(IntDub ib1, IntDub ib2) {
                if (ib1.getDub() > ib2.getDub()) {
                    return 1;
                }
                if (ib1.getDub() < ib2.getDub()) {
                    return -1;
                }
                return 0;
            }
        };
        PriorityQueue<IntDub> dist = new PriorityQueue<>(ComparatorDouble);
        HashMap<List<Integer>, Double> h = new HashMap<List<Integer>, Double>();
        List<NodeData> l = new ArrayList<NodeData>();
        Iterator<NodeData> it1 = this.myGraph.nodeIter();
        //filling in initial distance
        while (it1.hasNext()) {
            Vertex v1 = (Vertex) it1.next();
            List<Integer> l1 = new ArrayList<Integer>();
            l1.add(src);
            l1.add(v1.getKey());
            h.put(l1, Double.MAX_VALUE);
            l.add(v1);
        }
        // the src gets dist 0
        List<Integer> l1 = new ArrayList<Integer>();
        l1.add(src);
        l1.add(src);
        h.put(l1, 0.0);
        IntDub a = new IntDub(src, 0.0);
        dist.add(a);
        while (dist.size() > 0) {
            int peek = dist.peek().getInt();
            Iterator<EdgeData> it2 = this.myGraph.edgeIter(peek);
            while (it2.hasNext()) {
                Edge e = (Edge) it2.next();
                if (l.contains(myGraph.Nodes.get(e.getDest()))) {
                    List<Integer> l2 = new ArrayList<Integer>();
                    l2.add(src);
                    l2.add(e.getDest());
                    if (h.get(l2) == Double.MAX_VALUE) {
                        List<Integer> l3 = new ArrayList<Integer>();
                        l3.add(src);
                        l3.add(e.getSrc());
                        h.put(l2, h.get(l3) + e.getWeight());
                        double d = h.get(l3) + e.getWeight();
                        IntDub b = new IntDub(e.getDest(), d);
                        dist.add(b);
                    } else {
                        List<Integer> l3 = new ArrayList<Integer>();
                        l3.add(src);
                        l3.add(e.getSrc());
                        if (h.get(l2) > h.get(l3) + e.getWeight()) {
                            h.put(l2, h.get(l3) + e.getWeight());
                            double d = h.get(l3) + e.getWeight();
                            IntDub b = new IntDub(e.getDest(), d);
                            dist.add(b);
                        }
                    }
                }
            }
            dist.poll();
            Vertex n = this.myGraph.Nodes.get(peek);
            l.remove(n);
        }
        l1.clear();
        l1.add(src);
        l1.add(dest);
        return h.get(l1);
    }
*/


    /**
     git
     * @param src
     * @return
     */



    private Pair dijkstaList(int src) {
        Comparator<IntDub> ComparatorDouble = new Comparator<IntDub>() {
            @Override
            public int compare(IntDub ib1, IntDub ib2) {
                if (ib1.getDub() > ib2.getDub()) {
                    return 1;
                }
                if (ib1.getDub() < ib2.getDub()) {
                    return -1;
                }
                return 0;
            }
        };
        PriorityQueue<IntDub> dist = new PriorityQueue<>(ComparatorDouble);
        HashMap<List<Integer>, Double> h = new HashMap<List<Integer>, Double>();
        HashMap<List<Integer>, List<Integer>> hList = new HashMap<List<Integer>, List<Integer>>();
        List<NodeData> l = new ArrayList<NodeData>();
        Iterator<NodeData> it1 = this.myGraph.nodeIter();
        //filling in initial distance
        while (it1.hasNext()) {
            Vertex v1 = (Vertex) it1.next();
            List<Integer> l1 = new ArrayList<Integer>();
            l1.add(src);
            l1.add(v1.getKey());
            h.put(l1, Double.MAX_VALUE);
            hList.put(l1, l1);
            l.add(v1);
        }
        // the src gets dist 0
        List<Integer> l1 = new ArrayList<Integer>();
        l1.add(src);
        l1.add(src);
        h.put(l1, 0.0);
        IntDub a = new IntDub(src, 0.0);
        dist.add(a);
        while (dist.size() > 0) {
            int peek = dist.peek().getInt();
            Iterator<EdgeData> it2 = this.myGraph.edgeIter(peek);
            while (it2.hasNext()) {
                Edge e = (Edge) it2.next();
                if (l.contains(myGraph.Nodes.get(e.getDest()))) {
                    List<Integer> l2 = new ArrayList<Integer>();
                    l2.add(src);
                    l2.add(e.getDest());
                    if (h.get(l2) == Double.MAX_VALUE) {
                        List<Integer> l3 = new ArrayList<Integer>();
                        l3.add(src);
                        l3.add(e.getSrc());
                        h.put(l2, h.get(l3) + e.getWeight());
                        double d = h.get(l3) + e.getWeight();
                        IntDub b = new IntDub(e.getDest(), d);
                        dist.add(b);
                        List<Integer> l5 = new ArrayList<Integer>();
                        List<Integer> l6 = hList.get(l3);
                        for (int i = 0; i < l6.size(); i++) {
                            if (!l5.contains(l6.get(i))) {
                                l5.add(l6.get(i));
                            }
                        }
                        if (!l5.contains(e.getDest())) {
                            l5.add(e.getDest());
                        }
                        hList.put(l2, l5);
                    } else {
                        List<Integer> l3 = new ArrayList<Integer>();
                        l3.add(src);
                        l3.add(e.getSrc());
                        if (h.get(l2) > h.get(l3) + e.getWeight()) {
                            h.put(l2, h.get(l3) + e.getWeight());
                            double d = h.get(l3) + e.getWeight();
                            IntDub b = new IntDub(e.getDest(), d);
                            dist.add(b);
                            List<Integer> l5 = new ArrayList<Integer>();
                            List<Integer> l6 = hList.get(l3);
                            for (int i = 0; i < l6.size(); i++) {
                                if (!l5.contains(l6.get(i))) {
                                    l5.add(l6.get(i));
                                }
                            }
                            l5.add(e.getDest());
                            hList.put(l2, l5);
                        }
                    }
                }
            }
            dist.poll();
            Vertex n = this.myGraph.Nodes.get(peek);
            l.remove(n);
        }

        Pair p = new Pair(h, hList);
        return p;
        }


}