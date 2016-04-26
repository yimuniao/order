package com.yimuniao.algorithm;
import java.util.Random;  
public class HeapSortUtil {  
  
  
    /** 
     * �ö����򷽷�  �ҳ�ǰN�������� 
     * @originalArray ԭʼ�������� 
     * @topN ��Ҫȡ�õ�N������� 
     * @return  ����topN������������� 
     */  
    public int[] getTopArray(int[] originalArray,int topN){  
        int len = originalArray.length ;  
        if(len <= topN){  
            return  originalArray;  
        }  
        int[] array = new int [topN];  
        initHeap(originalArray);  
        int temp;  
        for(int i=0;i<array.length;i++){  
            array[i]= originalArray[0];  
            temp=originalArray[originalArray.length-i-1];  
            originalArray[originalArray.length-i-1]=originalArray[0];  
            originalArray[0]=temp;  
            buildHeap(0,originalArray.length-i-1,originalArray);  
        }  
  
  
        return array;  
    }  
    /** 
     * ������ʼ����� 
     */  
    private void initHeap(int[] orignalArr){  
        for(int i=orignalArr.length-1;i>=0;i--){  
            buildHeap(i,orignalArr.length,orignalArr);  
        }  
    }  
    /** 
     * ������ 
     * @param location ��ʼλ�� 
     * @param unSortLength ����ѵĳ��� 
     */  
    private void buildHeap(int location,int unSortLength,int[] arr){  
        int temp;  
        int tempLoc;  
        //�жϸø��ڵ��Ƿ������Һ���  
        if((tempLoc = (location+1)*2)<unSortLength){  
            if(arr[tempLoc]>arr[tempLoc-1]){//����ҽڵ������ڵ�  
                if(arr[tempLoc]>arr[location]){//����ҽڵ���ڸ��ڵ�  ��˫������ֵ  
                    temp = arr[location];  
                    arr[location] = arr[tempLoc];  
                    arr[tempLoc] = temp;  
                    buildHeap(tempLoc,unSortLength,arr);//�ݹ�  
                }  
            }else{//�����ڵ�����ҽڵ�  
                if(arr[tempLoc-1]>arr[location]){//�����ڵ���ڸ��ڵ�  
                    temp = arr[location];  
                    arr[location] = arr[tempLoc-1];  
                    arr[tempLoc-1] = temp;  
                    buildHeap(tempLoc-1,unSortLength,arr);//�ݹ�  
                }  
            }  
        }else if((tempLoc =((location+1)*2-1))<unSortLength){//����ø��ڵ�����ڵ�  
            if(arr[tempLoc]>arr[location]){//����ҽڵ���ڸ��ڵ�  
                temp = arr[location];  
                arr[location] = arr[tempLoc];  
                arr[tempLoc] = temp;  
                buildHeap(tempLoc,unSortLength,arr);//�ݹ�  
            }  
        }  
    }  
  
  
    public static void main(String[] args) {  
        int[] arr =new int[100000];  
        Random ran = new Random();  
        for (int i = 0; i < arr.length; i++) {  
            arr[i] = ran.nextInt(100000);  
        }  
        HeapSortUtil h  = new HeapSortUtil();  
        long start =  System.currentTimeMillis();  
        int topArr[] = h.getTopArray(arr, 20);  
        //��ӡ������������  
        for(int i=0;i<topArr.length;i++){  
            System.out.println(topArr[i]);  
        }  
  
  
        long end = System.currentTimeMillis()-start;  
        System.out.println("Total time��" + end + "ms");  
    }  
}  