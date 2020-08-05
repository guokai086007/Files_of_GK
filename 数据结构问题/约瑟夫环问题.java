package cn.gk.datastru;

//import com.greenpineyu.fel.parser.FelParser.conditionalAndExpression_return;

/**
 * Լɪ����Ϸ
 * ���ݽṹ������������
 * ��Ϸ������
 *    ����Ϊ1��2��3��... ��n��n����Χ��һȦ��Լ�����Ϊk(1<=k<=n)���˴�1��ʼ����������m���Ǹ��˳��У�
 *    ������һλ�ִ�1��ʼ����������m���Ǹ����ֳ��С��������ƣ�ֱ�������˳���Ϊֹ���ɴ˲���һ�����ӱ�ŵ����С�
 * @author ����
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
	 * ���컷������ķ���
	 * @param nums ��ʾ��������Ļ��������м����ڵ�
	 */
	public void createLoop(int nums)
	{
		if(nums<=0){
			System.out.println("�ڵ㲻��С�ڵ���0��");
			return;
		}
		
		Node cur = null;//����һ��������curָ�룬���㹹�컷������
		for(int i=1;i<=nums;i++)
		{
			if(i==1)//��һ���ڵ�
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
	 * ������������
	 */
	public void showLoop()
	{
		if(first==null){
			System.out.println("��������Ϊ�գ�");
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
		int n = 5;//һ����n����
		loop.createLoop(n);
		loop.showLoop();
		System.out.println("\n-----------------------------------------------");
		int m=2;//ÿ������m�����˳�
		OutLoop(loop,n,m);
	}
	
	/**
	 * ģ��Լɪ����Ϸ�ĺ���
	 * @param loop ���ڽ���Լɪ����Ϸ�Ļ�������
	 */
	public static void OutLoop(CircleSingleLinkedList loop,int n,int m)
	{
		Node first = loop.getFirst();
		
		Node cur = first;//����һ������
		int num = 1;//������1��ʼ��
		
		int cnt = n;
		while(cnt>0)
		{
			if(cur.isFlag()==true)//�ýڵ��Ѿ�����̭
			{
				cur = cur.getNext();
				continue;
			}
			if(num == m)//��ǰ�ڵ�Ӧ�ñ���̭
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
class Node//�ڵ���
{
	private Integer number;
	private Node next;
	private boolean flag = false;//�ñ�ʶ��ʾ�Ƿ���̭
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