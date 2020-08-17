package date_20200817;

import java.util.Scanner;
/**
 * ���� �㷨��� ���ΰ�
 *   ��������
�����ھ��ε��������κ����ﶼ�Ǿ��εģ����ε�ǹ�У��������ε��ӵ������ڸ���һ��N*M��ǹ�У�ͬʱ�������ӵ��Ĵ�СΪ(2l+1)*(2r+1)������һ��01��ͼÿ�����01״̬�ֱ��ʾ������Ƿ��ӵ������Ļ��У�1��ʾ�����У�0��û�У�һ��һ���㱻�ӵ������Ļ��У���ô�������Ϊ���� (2l+1)*(2r+1) ��Χ�ڰ����ϵĵ㶼�ᱻ���١�Ҫ��������հ��ӵ�״̬��
�����ʽ
������һ��ΪN��M��L,R��ʾ���ӵĴ�С���Լ��ӵ��Ĵ�С��
�����������һ��N*M��01�����ʾÿ�����Ƿ��ӵ������Ļ���
�����ʽ
����N*M��01�����ʾ�����ϵ�ÿ�����Ƿ��ƻ���
��������
	4 4 1 1
	1000
	0000
	0000
	0010
�������
	1100
	1100
	0111
	0111
 * @author ����
 *
 */
public class Main02
{
	public static int N,M,L,R;//��ʾ���ӵĴ�С���Լ��ӵ��Ĵ�С
	
	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		
		N = sc.nextInt();
		M = sc.nextInt();
		L = sc.nextInt();
		R = sc.nextInt();
		
		//������ӵ�ԭʼ���
		int a[][] = new int[N][M];
		
		for(int i=0;i<N;i++)
		{
			String s = sc.next();
			for(int j=0;j<M;j++)
			{
				a[i][j] = Integer.parseInt(s.charAt(j)+"");
			}
		}
		
		//��һ��b�����ȵ���a����Ȼ�����a�����1��λ��ȥ�޸�b����
		int b[][] = new int[N][M];
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<M;j++)
			{
				b[i][j] = a[i][j];
			}
		}
		
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<M;j++)
			{
				if(a[i][j] == 1)
				{
					f(b,i,j);
				}
			}
		}
		
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<M;j++)
			{
				System.out.print(b[i][j]);
			}
			System.out.println();
		}
		
		sc.close();
	}
	
	/**
	 * ��b�������ԣ�i,j����Ϊ���ģ��ԣ�i-1,j-r��Ϊ���϶��㣬�ԣ�i-l,j+r��Ϊ���϶��㣬�ԣ�i+l,j-r��Ϊ���¶��㣬�ԣ�i+l,j+r��Ϊ���¶���
	 * ��������Ϊ1��
	 * @param b
	 * @param i
	 * @param j
	 * @param l
	 * @param r
	 */
	private static void f(int b[][], int i, int j) 
	{
		
		for(int x=(i-L); x<=(i+L); x++)
		{
			if(x<0) continue;
			if(x>=N) break;
			for(int y=(j-R); y<=(j+R); y++)
			{
				if(y<0) continue;
				if(y>=M) break;
				b[x][y] = 1;
			}
		}
		
	}
	
}
