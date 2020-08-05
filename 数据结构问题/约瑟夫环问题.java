package cn.gk.datastru;

//import com.greenpineyu.fel.parser.FelParser.conditionalAndExpression_return;

/**
 * 约瑟夫游戏
 * 数据结构：单向环形链表
 * 游戏描述：
 *    设编号为1、2、3、... 、n的n个人围坐一圈，约定编号为k(1<=k<=n)的人从1开始报数，数到m的那个人出列，
 *    它的下一位又从1开始报数，数到m的那个人又出列。依次类推，直到所有人出列为止，由此产生一个出队编号的序列。
 * @author 郭凯
 *
 */
public class CircleSingleLinkedList {
	
	private Node first = null;
	
	public Node getFirst() {
		return first;
	}

	public void setFirst(Node first) {
		this.first = first;
	}

	/**
	 * 构造环形链表的方法
	 * @param nums 表示构造出来的环形链表有几个节点
	 */
	public void createLoop(int nums)
	{
		if(nums<=0){
			System.out.println("节点不能小于等于0！");
			return;
		}
		
		Node cur = null;//定义一个辅助的cur指针，方便构造环形链表
		for(int i=1;i<=nums;i++)
		{
			if(i==1)//第一个节点
			{
				first = new Node(i);
				first.setNext(first);
				cur = first;
			}
			else
			{
				Node node = new Node(i);
				cur.setNext(node);
				node.setNext(first);
				cur = node;
			}
		}
	}
	
	/**
	 * 遍历环形链表
	 */
	public void showLoop()
	{
		if(first==null){
			System.out.println("环形链表为空！");
			return;
		}
		
		Node cur = first;
		while(true)
		{
			if(cur.getNext()!=first) {
				System.out.print(cur.getNumber()+"->");
				cur = cur.getNext();
			}else{
				System.out.print(cur.getNumber()+"->"+cur.getNext().getNumber());
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		
		CircleSingleLinkedList loop = new CircleSingleLinkedList();
		int n = 5;//一共有n个人
		loop.createLoop(n);
		loop.showLoop();
		System.out.println("\n-----------------------------------------------");
		int m=2;//每次数到m的人退出
		OutLoop(loop,n,m);
	}
	
	/**
	 * 模拟约瑟夫游戏的函数
	 * @param loop 用于进行约瑟夫环游戏的环形链表
	 */
	public static void OutLoop(CircleSingleLinkedList loop,int n,int m)
	{
		Node first = loop.getFirst();
		
		Node cur = first;//设置一个辅助
		int num = 1;//从数字1开始数
		
		int cnt = n;
		while(cnt>0)
		{
			if(cur.isFlag()==true)//该节点已经被淘汰
			{
				cur = cur.getNext();
				continue;
			}
			if(num == m)//当前节点应该被淘汰
			{
				cur.setFlag(true);
				num = 1;
				cnt--;
				System.out.print(cur.getNumber()+" ");
				cur = cur.getNext();
				continue;
			}
			num++;
			cur = cur.getNext();
		}
	}

}
class Node//节点类
{
	private Integer number;
	private Node next;
	private boolean flag = false;//该标识表示是否被淘汰
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Node getNext() {
		return next;
	}
	public void setNext(Node next) {
		this.next = next;
	}
	public Node(Integer number) {
		super();
		this.number = number;
	}
}