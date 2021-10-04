package PCcamera;

public class Linked <T>{//一个java文件中最多只能有一个public类
    public class Node{
        private T t;
        private Node next;
        public Node(T t,Node next){
            this.t = t;
            this.next = next;
        }
        public Node(T t){ //尾节点
            this(t,null);
        }

        public T getT() {
            return t;
        }
    }
    private Node head;    		//头结点
    private int size;			//链表元素个数
    //构造函数
    public Linked(){
        this.head = null;
        this.size = 0;
    }

    public Node getHead() {
        return head;
    }

    public int getSize() {
        return size;
    }

    public void addFirst(T t){
        Node n = new Node(t);
        n.next = this.head;
        this.head=n; //next指向自己 head指向自己
        this.size++;
    }

    public void addLast(T t){
        add(t,this.size);
    }

    public void add(T t,int index){
        if(index<0||index>size){
            throw new IllegalArgumentException("index is error");
        }
        if(index==0){
            addFirst(t);
        }
        Node preNode = this.head;
        //找到要插入节点的前一个节点
        for(int i = 0; i < index-1; i++){
            preNode = preNode.next;
        }
        Node node = new Node(t);
        node.next=preNode.next; //先把原来preNode 的下一节点赋给新加入的节点
        preNode.next = node; //再把preNode下一节点指向新节点 顺序不能乱
        this.size++;

    }
    //删除指定含数据的节点
    public void delete(T t){
        if (this.head==null){ //无头结点
            System.out.println("链表为空,不能删除");
        }
        if (this.head.t==t){
            //删除头结点
            this.head = head.next;
            this.size--;
        }
        Node preNode = head;
        //从头遍历结点查找对应值
        while (preNode.t!=t&&preNode.next!=null){
            if (preNode.next.t.equals(t)){
                preNode.next =preNode.next.next; //传递指针到下一个结点
                size--;
                break; //去掉break可以删除所有数据等于t的节点，不带则只删掉离头结点最近的
            }
            else
                preNode = preNode.next;
        }
    }
    public T deleteLast(){
        T t=null;
        if(this.size==1){
            System.out.println("删除唯一元素");
            t=head.t;
            return t;
        }else if(this.head==null){
            System.out.println("无元素可删除");
        } else {
            Node preNode = head;
            Node cur =head;
            while (cur.next!=null) {
                    preNode = cur;
                    cur = cur.next;
            }
            preNode.next=cur.next;
            t= cur.t;
            size--;
        }
        return t;
    }

    public T deleteFirst(){
        if (head!=null){
        T temp;
        temp = head.t;
        this.head = head.next;
        size--;
        return temp;
    }else
        return null;
    }




    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        //和 String 类不同的是，StringBuffer 和 StringBuilder 类的对象能够被多次的修改，并且不产生新的未使用对象。
        Node cur = this.head;
        while(cur != null){
            sb.append(cur.t+"->");
            cur = cur.next;
        }
        sb.append("NULL");
        return sb.toString();
    }

    public static void main(String[] args) {
        Linked<Integer> linked = new Linked();
        for(int i = 0; i < 10; i++){
            linked.addFirst(i);
            //System.out.println(linked);
        }
        linked.addLast(33);
        System.out.println(linked);
        System.out.println("===");
        //linked.addFirst(1);
        linked.add(2, 1);
        System.out.println(linked);
        System.out.println("====");
        linked.delete(2);
        System.out.println(linked);
        System.out.println("=====");
        System.out.println("删除第一个元素："+linked.deleteFirst());
        System.out.println(linked);
        System.out.println("删除最后一个元素："+linked.deleteLast());
        System.out.println(linked);
    }

}