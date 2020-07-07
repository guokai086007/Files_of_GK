package cn.gk.test;

import java.util.Scanner;

/**
 * �㷨��� �ַ����Ĳ���
 * 	��Դ����
	ʱ�����ƣ�1.0s   �ڴ����ƣ�256.0MB
	��������
	��������һ���ַ���S��Ȼ�����q��ָ��ֱ���4�֣�
	
	����1. Append str
	������ʾ��S�����׷��һ���ַ���str��
	��������
	����ԭ�ַ�����ABCDE
	����ִ�� Append FGHIJ ��
	�����ַ�����Ϊ��ABCDEFGHIJ
	
	����2. Insert x str
	������ʾ��λ��x������һ���ַ���str�������뱣֤0<x<=��ǰ�ַ������ȣ�
	��������
	����ԭ�ַ�����ABCGHIJ
	����ִ�� Insert 4 DEF ��
	�����ַ�����Ϊ��ABCDEFGHIJ
	
	����3. Swap a b c d
	������ʾ�����ӵ�aλ����bλ���ַ�����ӵ�cλ����dλ���ַ����������뱣֤0<a<b<c<d<=��ǰ�ַ������ȣ�
	��������
	����ԭ�ַ�����ABGHIFCDEJ
	����ִ�� Swap 3 5 7 9��
	�����ַ�����Ϊ��ABCDEFGHIJ
	
	����4. Reverse a b
	������ʾ���ӵ�aλ����bλ���ַ�����ת�������뱣֤0<a<b<=��ǰ�ַ������ȣ�
	��������
	����ԭ�ַ�����ABGFEDCHIJ
	����ִ�� Reverse 3 7 ��
	�����ַ�����Ϊ��ABCDEFGHIJ
	
	������������˳��ִ����ָ�����ַ�����
	�����ʽ
	���������һ�а����ַ���S���ڶ��а���һ������q��������q�зֱ�Ϊq��ָ�
	�����ʽ
	�������Ϊ1�У�Ϊ��˳��ִ��������ָ�����ַ�����
	��������
		My
		5
		Append Hello
		Insert 3 dlroW
		Reverse 3 7
		Swap 3 7 8 12
		Swap 1 2 3 7
	�������
		HelloMyWorld
	����˵��
	����ԭ�ַ�����My
	����ִ�� Append Hello ��MyHello
	����ִ�� Insert 3 dlroW ��MydlroWHello
	����ִ�� Reverse 3 7 ��MyWorldHello
	����ִ�� Swap 3 7 8 12 ��MyHelloWorld
	����ִ�� Swap 1 2 3 7 ��HelloMyWorld
	���ݹ�ģ��Լ��
	��������30%�����ݣ�q=1��
	
	��������70%�����ݣ�����Swapָ�Swapָ����b-a=d-c��
	
	��������100%�����ݣ������ַ������Ȳ�����40000��1<=q<=150
 * @author ����
 *
 */
public class Main {

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		
		String str = sc.nextLine();//����һ����ʼ�ַ���
		int n = sc.nextInt();//�������������   ��n������
		
		String ss = sc.nextLine();//��n��������Ļس��Ե�����Ȼ�������ִ���
		StringBuilder sb = new StringBuilder(str);//��ʹ��һ��StringBuilder���͵Ķ������֮��Ĳ������
		while((n--)>0)
		{
			String command = sc.nextLine();//ÿ������һ������
			if(command.contains("Append"))//���ӵ�����
			{
				String temp = command.split(" ")[1];//�ÿո������ֿ���ȥ��������Ҫ���ӵ��ַ���
				str = sb.append(temp).toString();//���ַ��������ϣ��ڸ�ֵ��str
			}
			else if(command.contains("Insert"))//��������
			{
				int index = Integer.parseInt(command.split(" ")[1]);//��ȡ�����λ��
				String temp = command.split(" ")[2];//��ȡ������ַ���
				str = sb.insert(index-1, temp).toString();//����StringBuilder�����insert�������в��룬��Ϊ��Ŀ�Ǵ�1��ʼ�����½Ǳ�ģ���������index-1
			}
			else if(command.contains("Swap"))//��������
			{
				String strs[] = command.split(" ");//��������ȡabcd�ĸ�λ��
				int a = Integer.parseInt(strs[1]);
				int b = Integer.parseInt(strs[2]);
				int c = Integer.parseInt(strs[3]);
				int d = Integer.parseInt(strs[4]);
				
				String s1 = str.substring(a-1,b);//��ȡa��b���ַ���
				String s2 = str.substring(c-1,d);//��ȡc��d���ַ���
				
				/*
				 * ����Ƚϳ���
				 * Ҫ����a��b��c��d ����0��ʼ���½Ǳ�Ӧ����a-1��b-1  c-1��d-1��
				 *  1.�������Ȱ�a-1֮ǰ�ı���
				 *  2.s2����c-1��d-1ֱ�Ӽ��ڵ�һ���ĺ���
				 *  3.s2�Ѿ������˺��棬���ԾͲ�Ҫa-1��b-1�ˣ��ǾͰѴ�b��ʼ��c-1�����ݱ���,����a-1��b-1�ͱ�s2�滻 ��
				 *  4.Ȼ�������ԭ��Ӧ���� c-1��d-1������s1���ں���
				 *  5.����ԭ��d-1֮��ı���������������ֽ���
				 */		
				str = str.substring(0,a-1) + s2 +str.substring(b, c-1) + s1 + str.substring(d);
				sb = new StringBuilder(str);
			}
			else//��ת����
			{
				String strs[] = command.split(" ");//��������ȡab������λ��
				int a = Integer.parseInt(strs[1]);
				int b = Integer.parseInt(strs[2]);
				
				String s1 = str.substring(a-1, b);//��ȡa-1��b���Ӵ�
				s1 = new StringBuilder(s1).reverse().toString();//���Ӵ���ת��ȡ��ת��Ľ��
				sb.replace(a-1, b, s1);//������Ϊ��ת�󳤶�һ��һ��������ֱ��ʹ��StringBuilder�����replace�����滻ԭ�������λ�þ�����
				str = sb.toString();//�ѷ�ת��Ľ�����ظ�str
			}
		}
		
		System.out.println(str);
		sc.close();
	}

}
