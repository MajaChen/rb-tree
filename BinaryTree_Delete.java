/*
author:chxy
data:2020/4/20
description:
*/
package binarytreedelete;

/*
* 二叉树结点的删除：
* 1.该结点既无左子树又无右子树
*  是根结点
*  是非根结点
* 2.该结点有左子树或者右子树
* 3.该结点既有左子树，又有右子树
* */

import java.util.*;

class TreeNode{

    int val;
    TreeNode left;

    TreeNode right;

    public TreeNode(int val){
        this.val = val;
        left = null;
        right = null;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "val=" + val +

                '}';
    }
}

public class Solution13 {

    //采用先序遍历，寻找target的父结点
    private TreeNode preorderTraverse(TreeNode parent,TreeNode current, TreeNode target){

        if(current == null)return null;

        else {
            //当前结点等于target则返回parent
            if (current == target) return parent;
            else {//否则，递归遍历其左子树和右子树
                if (target.val > current.val)
                    return preorderTraverse(current, current.right, target);
                if (target.val < current.val)
                    return preorderTraverse(current, current.left, target);
            }
        }
        return null;
    }

    //在以root为根的子树中，寻找左子树的最右下结点
    private TreeNode findLeftRight(TreeNode root){

        TreeNode p = root.left,q = null;
        while(p != null){
            q = p;
            p = p.right;
        }
        return q;
    }


    private void delete1(TreeNode root,TreeNode target){

        //待删除的不是根结点则需要找到target的父结点
        TreeNode parent = preorderTraverse(null,root,target);

        if(parent == null){ //待删除的正是根结点
            root = null;
            return;
        }

        //从父节点删除target
        if(target.val < parent.val)
            parent.left = null;
        else if(target.val > parent.val)
            parent.right = null;

        target = null;
        return;
    }

    //用它的左/右子树代替target结点即可
    private void delete2(TreeNode root,TreeNode target){

        TreeNode child = (target.left == null)?target.right:target.left;

        //获取父结点，归根结底还是要从父结点进行删除
        TreeNode parent = preorderTraverse(null,root,target);

        if(parent == null){//要删除的本身就是父结点
            root = child;
            return;
        }

        if(target.val < parent.val){//待删结点在父节点的左子树
            parent.left = child;
            return;
        }

        if(target.val > parent.val){//待删结点在父节点的右子树
            parent.right = child;
            return;
        }
    }

    private void delete3(TreeNode root,TreeNode target){
        //用中序遍历的前任结点来代替该结点
        //即左子树的最右下结点，左子树中最大的结点
        // 该结点要么是叶结点，要么是只有左子树的结点
        TreeNode predecessor = findLeftRight(target);

        int holder = predecessor.val;
        deleteFromBinarytee(root,predecessor);
        //用predecessor的值来代替target的值
        target.val = holder;
        return;
    }

    //从以root为根结点的二叉排序树中删除target结点
    public void deleteFromBinarytee(TreeNode root,TreeNode target){

        int left = (target.left == null)?0:1;
        int right = (target.right == null)?0:1;
        switch (left+right) {
            case 0://左右子树均空
                delete1(root,target);
                break;
            case 1://左右子树有一个不空
                delete2(root,target);
                break;
            case 2://左右子树都不空
                delete3(root,target);
        }
    }

    public void inorderTraverse(TreeNode root,List<Integer> list){

        if(root != null){
            inorderTraverse(root.left,list);
            list.add(root.val);
            inorderTraverse(root.right,list);
        }
    }

    public static void main(String[] args) {

        Solution13 s = new Solution13();

        TreeNode four = new TreeNode(4);
        TreeNode two = new TreeNode(2);
        TreeNode five = new TreeNode(5);
        TreeNode one = new TreeNode(1);
        TreeNode three = new TreeNode(3);

        four.left = two;
        four.right = five;
        two.left = one;
        two.right = three;

        List<Integer> list = new ArrayList<>();
        s.inorderTraverse(four,list);
        System.out.println(list);

        s.deleteFromBinarytee(four,four);

        list.clear();
        s.inorderTraverse(four,list);
        System.out.println(list);
    }
}


