package cn.gk.test02;

import java.util.Scanner;

/**
 * ���� �㷨��� ���˻ῪĻʽ
 * 	��������
����ѧУ����һ�������������һ�����ȥ�μӰ��˻�Ŀ�Ļʽ��ÿ���˶�����Ҫȥ����������ֻ��һ������ô�죿�೤�����һ���취��
	�ð��ϵ�����ͬѧ������n��ͬѧ��Χ��һȦ������˳ʱ�뷽����б�š�Ȼ�����ѡ��һ����m�����Ҵ�1��ͬѧ��ʼ����˳ʱ�뷽�����α�����
	1, 2, ��, m��������m��ͬѧ����Ҫ�����˳�Ȧ�ӡ�Ȼ��ͣ�ذ�˳ʱ�뷽����һ�ñ���m�߳�Ȧ�����ʣ�µ��Ǹ��˾���ȥ�μӿ�Ļʽ���ˡ�
����Ҫ���û�������ķ�������⡣��ν������������������β��㣬��nextָ����ָ����������׽�㡣����˼·���ȴ���һ����������
		ģ����ͬѧΧ��һȦ�����Ρ�Ȼ�����ѭ����̭���ڣ�ģ���1��m������ÿ����һλͬѧ����㣩�˳�Ȧ�ӡ�
���������ʽ������ֻ��һ�У�������������n��m������n�� m�ĺ�������������
���������ʽ�����ֻ��һ�����������μӿ�Ļʽ���Ǹ��˵ı�š�
	�����������
	��������
	8 3
	�������
	7
 * 
 * @author ����
 *
 */
public class Main03 
{

	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		sc.close();
		
		Student head = new Student(0);
		//ʹ��ָ��p����ͷָ��
		Student p = head;
		
		for(int i=1;i<=n;i++)
		{
			//ÿ�δ���һ������
			Student cur = new Student(i);
			//��pָ�������������������һ������
			p.setNext(cur);
			//����p��λ��
			p = p.getNext();
		}
		//����forѭ��ʱ���ǵ����������ڵ����ɻ���
		head = head.getNext();//��headָ��1
		p.setNext(head);//�������һ������һ����1
		
		int cnt = n;//ʣ������
		int num = 1;//��1��ʼ����
		
		//��curָ�����headָ�룬��ʼ���б�����̭����
		Student cur = head;
		while(cnt>1)
		{
			if(cur.isFlag()==true){//��ѧ���Ѿ�����̭��
				cur = cur.getNext();
				continue;
			}
			if(num == m)
			{
				cur.setFlag(true);//�ѵ�ǰѧ����̭
				cnt--;
				num = 1;
				cur = cur.getNext();
				continue;
			}
			num++;
			cur = cur.getNext();
		}
		
		for(int i=1;i<=n;i++)
		{
			if(head.isFlag()==false) System.out.println(head.getNumber());
			head = head.getNext();
		}
	}
}
class Student{
	private Integer number;
	private boolean flag = false;//����Ƿ�ɾ��
	private Student next;//��һ��ѧ��
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public Student getNext() {
		return next;
	}
	public void setNext(Student next) {
		this.next = next;
	}
	public Student(Integer number) {
		super();
		this.number = number;
	}
}