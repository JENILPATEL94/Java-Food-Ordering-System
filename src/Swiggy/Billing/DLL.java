package Swiggy.Billing;

public class DLL
{
    static class node
    {
        node next,prev;
        int data;
        int quantity;
        node(int data,int quantity)
        {
            this.data=data;
            this.quantity=quantity;
            next=prev=null;
        }
    }
    public node first=null;


    public void addToCart(int data,int quantity)
    {
        node n = new node(data,quantity);
        if (quantity>0) {
            if (first == null) {
                first = n;
            } else {
                node temp = first;
                while (temp.next != null) {
                    temp = temp.next;
                }
                temp.next = n;
                n.prev = temp;
            }
        }
    }

    void removeItem(int val,int quantity)
    {
        if (quantity<1)
        {
            System.out.println("Enter valid quantity to remove");
            return;
        }
        boolean flag=false;
        node dummy=first;
        while (dummy!=null)
        {
            if (dummy.data==val)
            {
                flag = true;
            }
            dummy=dummy.next;
        }
        if (flag==false)
        {
            System.out.println("No such item present");
        }
        else {
            node temp1=first;
            while (temp1.data!=val)
            {
                temp1=temp1.next;
            }
            if (temp1.quantity==quantity) {
                if (first.data == val) {

                    if (first.next == null) {
                        first = null;
                    } else {
                        node dell = first;
                        first = first.next;
                        first.prev = null;
                        dell.next = null;
                    }
                } else {
                    node temp = first;
                    while (temp.next.data != val) {
                        temp = temp.next;
                    }
                    node del = temp.next;
                    if (del.next == null) {
                        temp.next = null;
                        del.prev = null;
                    } else {
                        temp.next = del.next;
                        del.next.prev = temp;
                        del.prev = null;
                        del.next = null;
                    }
                }
            }else if(temp1.quantity<quantity){
                System.out.println("Quantity is should be less then quantity present in cart");
            }
            else {
                temp1.quantity-=quantity;
            }
        }
    }

    public boolean itemPresent(int id)
    {
        boolean flag=false;
        node dummy=first;
        while (dummy!=null)
        {
            if (dummy.data==id)
            {
                flag = true;
            }
            dummy=dummy.next;
        }
        if (flag==false)
        {
            return false;
        }
        else {
            return true;
        }
    }
}
