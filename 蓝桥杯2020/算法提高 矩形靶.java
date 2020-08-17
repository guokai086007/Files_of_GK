package date_20200817;

import java.util.Scanner;
/**
 * 试题 算法提高 矩形靶
 *   问题描述
　　在矩形的世界里任何事物都是矩形的，矩形的枪靶，甚至矩形的子弹。现在给你一张N*M的枪靶，同时告诉你子弹的大小为(2l+1)*(2r+1)。读入一张01的图每个点的01状态分别表示这个点是否被子弹的中心击中（1表示被击中，0则没有）一旦一个点被子弹的中心击中，那么以这个点为中心 (2l+1)*(2r+1) 范围内靶子上的点都会被击毁。要求输出最终靶子的状态。
输入格式
　　第一行为N，M，L,R表示靶子的大小，以及子弹的大小。
　　下面读入一个N*M的01矩阵表示每个点是否被子弹的中心击中
输出格式
　　N*M的01矩阵表示靶子上的每个点是否被破坏掉
样例输入
	4 4 1 1
	1000
	0000
	0000
	0010
样例输出
	1100
	1100
	0111
	0111
 * @author 郭凯
 *
 */
public class Main02
{
	public static int N,M,L,R;//表示靶子的大小，以及子弹的大小
	
	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		
		N = sc.nextInt();
		M = sc.nextInt();
		L = sc.nextInt();
		R = sc.nextInt();
		
		//构造靶子的原始情况
		int a[][] = new int[N][M];
		
		for(int i=0;i<N;i++)
		{
			String s = sc.next();
			for(int j=0;j<M;j++)
			{
				a[i][j] = Integer.parseInt(s.charAt(j)+"");
			}
		}
		
		//用一个b矩阵先等与a矩阵，然后根据a矩阵的1的位置去修改b矩阵
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
	 * 将b数组中以（i,j）点为中心，以（i-1,j-r）为左上顶点，以（i-l,j+r）为右上顶点，以（i+l,j-r）为左下顶点，以（i+l,j+r）为右下顶点
	 * 的区域置为1。
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
