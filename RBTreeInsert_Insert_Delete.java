/*
author:chxy
data:2020/5/12
description:红黑树的插入操作
*/
package RBTree;



public class RBTreeInsert_Delete {

    public static RBTreeNode  root;

    //从根结点中序遍历，打印val
    public static void InorderTraverse(RBTreeNode root) {

        if (root != null) {
            InorderTraverse(root.left);

            int left = -1, right = -1, parent = -1;
            if (root.left != null) left = root.left.val;
            if (root.right != null) right = root.right.val;
            if (root.parent != null) parent = root.parent.val;
            System.out.println(root.val + "\t left:" + left + "\t right:" + right + "\t parent:" + parent + "\t color:" + root.color);

            InorderTraverse(root.right);
        }
    }

    //以x为中心左旋
    public static void leftRotate(RBTreeNode x){

        RBTreeNode y = x.right;

        x.right = y.left;//将y的左子树拼接到x的右子树
        if(y.left != null)
            y.left.parent = x;

        y.parent = x.parent;
        if(x.parent == null){//x本身是根结点
            root = y;
        }else if(x == x.parent.left){
            x.parent.left = y;
        }else x.parent.right = y;

        y.left = x;
        x.parent = y;
    }

    //以x为中心右旋
    public static void rightRotate(RBTreeNode x){

        RBTreeNode y = x.left;
        x.left = y.right;
        if(y.right != null)
            y.right.parent = x;

        y.parent = x.parent;
        if((x.parent == null)){//x本身是根结点
            root = y;
        }else if(x.parent.left == x){
            x.parent.left = y;
        }else x.parent.right = y;

        y.right = x;
        x.parent = y;

    }

    public static void insert(int val){

        //首先寻找插入位置,q记录p的父节点
        RBTreeNode p = root,q = null;
        while(p != null&&p.val != val){
            if(val > p.val){
                q = p;
                p = p.right;
            }else{
                q = p;
                p = p.left;
            }
        }
        //循环的结束条件要么是p为空，要么是p.val == val
        if(p == null){//作为q的子结点插入
            RBTreeNode z = new RBTreeNode(val);
            if(q == null){//q为空，插入结点作为根结点插入
                root = z;
                root.color = RBTreeNode.BLACK;
            }else if(val > q.val){//作为q的右孩子插入
                q.right = z;
                z.parent = q;
                InsertFixup(z);
            }else{//作为q的左孩子插入
                q.left = z;
                z.parent = q;
                InsertFixup(z);
            }

        }else if(p.val == val){
            //值已存在，不插入
            return;
        }
    }

    //插入结点之后修复颜色
    public static void InsertFixup(RBTreeNode z){

        while(z != root&&z.parent.color.equals(RBTreeNode.RED)){

            if(z.parent == z.parent.parent.left){
                RBTreeNode y = z.parent.parent.right;//叔父结点
                //case1,父结点、叔父结点染黑，祖父结点染红,z指向祖父结点
                if(y != null && y.color.equals(RBTreeNode.RED)){
                    z.parent.color = RBTreeNode.BLACK;
                    y.color = RBTreeNode.BLACK;
                    z.parent.parent.color = RBTreeNode.RED;
                    z = z.parent.parent;
                    continue;
                }else if(z == z.parent.right){//case2,左右，调整为左左
                    z = z.parent;
                    leftRotate(z);
                }
                //case3,父节点染黑，祖父结点染红，右旋
                z.parent.color = RBTreeNode.BLACK;
                z.parent.parent.color = RBTreeNode.RED;
                rightRotate(z.parent.parent);
            }else{
                RBTreeNode y = z.parent.parent.left;
                if(y != null && y.color.equals(RBTreeNode.RED)){
                    z.parent.color = RBTreeNode.BLACK;
                    y.color = RBTreeNode.BLACK;
                    z.parent.parent.color = RBTreeNode.RED;
                    z = z.parent.parent;
                    continue;
                }else if(z == z.parent.left){//右左，转换成右右
                    z = z.parent;
                    rightRotate(z);
                }
                z.parent.color = RBTreeNode.BLACK;
                z.parent.parent.color = RBTreeNode.RED;
                leftRotate(z.parent.parent);
            }
        }
        root.color = RBTreeNode.BLACK;
    }

    public static RBTreeNode findSuccessor(RBTreeNode z) {
        RBTreeNode p = z.right,q = null;
        while(p != null) {
            q = p;
            p = p.left;
        }

        return q;
    }

    private static void delete1(RBTreeNode z){
        //如果z是叶结点，则直接删除
        if(z.left == null && z.right == null){
            if(z.parent == null){//删除的是根结点
                root = null;
                return;
            }
            if(z.parent.left == z){
                z.parent.left = null;
                return;
            }
            if(z.parent.right == z){
                z.parent.right = null;
                return;
            }
        }
    }

    private static void delete2(RBTreeNode z) {

        RBTreeNode x,y = z;
        if(z.left != null)
            x = z.left;
        else x = z.right;

        //y==z,直接用x结点代替y结点，
        if(z.parent == null) {//删除的是根结点
            x.parent = null;
            root = x;
        }else {
            x.parent = y.parent;
            if(y.parent.left == y)
                y.parent.left = x;
            else if(y.parent.right == y)
                y.parent.right = x;
        }

        if(y.color == RBTreeNode.BLACK)
            deleteFixup(x);
    }

    private static void delete3(RBTreeNode z) {

        //用y结点的值代替z
        RBTreeNode y = findSuccessor(z);
        z.val = y.val;

        //再删除y，y要么是叶结点，要么是只有右子树的结点
        RBTreeNode x = y.right;
        if(y.parent.left == y){
            y.parent.left = x;
        }else y.parent.right = x;
        if(x != null)x.parent = y.parent;

        if(y.color.equals(RBTreeNode.BLACK)){
            deleteFixup(x);
        }
    }

    //删除结点之后修复颜色
    private static void deleteFixup(RBTreeNode x) {

        if(x == null)
            return;

        while(x != root && x.color.equals(RBTreeNode.BLACK)){

            if(x == x.parent.left){
                RBTreeNode w = x.parent.right;
                //兄弟结点是红色，进入情况一，转入情况二三四
                //这一步操作的目的在于将w调整为黑色
                if(w.color.equals(RBTreeNode.RED)){
                    w.color = RBTreeNode.BLACK;
                    x.parent.color = RBTreeNode.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                //兄弟结点是黑色而且兄弟结点的左右子节点都是黑色,进入情况2（黑黑）
                //将兄弟结点染红,x指向父节点
                if(w.left.color.equals(RBTreeNode.BLACK) &&
                        w.right.color.equals(RBTreeNode.BLACK)){
                    w.color = RBTreeNode.RED;
                    x = x.parent;
                }
                //兄弟结点是黑色，它的右子结点是黑色,进入情况3，转入情况4
                //这一步操作将右子结点要转变为红色
                else if (w.right.color.equals(RBTreeNode.BLACK)){
                    w.left.color = RBTreeNode.BLACK;
                    w.color = RBTreeNode.RED;
                    rightRotate(w);
                    w = w.parent.right;
                }
                //进入情况4，左子结点是红色
                w.color = x.parent.color;
                x.parent.color = RBTreeNode.BLACK;
                w.right.color = RBTreeNode.BLACK;
                leftRotate(x.parent);
                x = root;
            }else{
                RBTreeNode w = x.parent.left;
                if(w.color.equals(RBTreeNode.RED)){
                    w.color = RBTreeNode.BLACK;
                    x.parent.color = RBTreeNode.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if(w.left.color.equals(RBTreeNode.BLACK)&&
                        w.right.color.equals(RBTreeNode.BLACK)){
                    w.color = RBTreeNode.RED;
                    x = x.parent;
                }
                else if(w.left.color.equals(RBTreeNode.BLACK)){
                    w.right.color = RBTreeNode.BLACK;
                    w.color = RBTreeNode.RED;
                    leftRotate(w);
                    w = x.parent.left;
                }
                w.color = x.parent.color;
                x.parent.color = RBTreeNode.BLACK;
                w.left.color = RBTreeNode.BLACK;
                rightRotate(x.parent);
                x = root;
            }
        }
        x.color = RBTreeNode.BLACK;
    }



    public static void delete(RBTreeNode z){

        int left = (z.left == null)?0:1;
        int right = (z.right == null)?0:1;
        switch (left+right) {
            case 0://左右子树均空
                delete1(z);
                break;
            case 1://左右子树有一个不空
                delete2(z);
                break;
            case 2://左右子树都不空
                delete3(z);
        }
    }

    public static void main(String[] args) {

        insert(1);
        insert(5);
        insert(6);
        insert(7);
        insert(8);
        insert(9);
        insert(10);
        insert(11);
        insert(12);
        insert(13);
        insert(14);
        insert(15);

        System.out.println(root == null);

        //删除叶结点15
        //delete(root.right.right.right.right);

        //删除14
        delete(root.right.right.right);

        //删除9
        delete(root.right.left);

        //删除5
        delete(root.left);

        InorderTraverse(root);
    }
}


