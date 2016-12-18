/**
 * Copyright (C) 2013 - Vladimir Jimenez
 * License: MIT
 */

package main;

public class LinkedList<E>
{
    private Node<E> head = null;
    private Node<E> tail = null;
    private Node<E> temp = null;

    private int counter;

    public LinkedList()
    {
        counter = 0;
    }

    /**
     * Retrieve the number of elements in the linked list.
     *
     * @return The size of the linked list.
     */
    public int size ()
    {
        return counter;
    }

    /**
     * Add a new indeterminate to the current LinkedList
     *
     * @param _coefficient The coefficient we're storing
     * @param _exponent    The exponent we're storing
     */
    public void add (int _coefficient, int _exponent)
    {
        if (head == null) // List is empty, so let's add it to the front
        {
            head = tail = new Node<E>();     // Create new nodes for the head and the tail
            head.coefficient = _coefficient; // Set the coefficient
            head.exponent = _exponent;       // Set the exponent
            head.next = tail;                // Set the link for the next node to the null tail we just created
            tail = head;                     // Set the last element to the one we just added since it's the only element
        }
        else
        {
            tail.next = new Node<E>();       // Add a new node to the end of the list
            tail = tail.next;                // The last node is now the node we just created
            tail.coefficient = _coefficient; // Set coefficient to be stored to the end node
            tail.exponent = _exponent;       // Set the exponent to be stored
        }

        counter++;
    }

    /**
     * Find the coefficient belonging to the respective exponent
     *
     * @param exponent The exponent of the coefficient we're looking for
     *
     * @return The coefficient or 0 is nothing is found
     */
    public int getCoefficient(int exponent)
    {
        temp = head; // Let's start at the beginning

        for (int i = 0; i < size(); i++) // Loop through all the nodes
        {
            if (temp.exponent == exponent) // Check if we have found the node with our exponent
            {
                return temp.coefficient; // Return it as an int
            }

            temp = temp.next; // Move on to the next node
        }

        //In order to make life easier, we'll return zero if no coefficient is found because mathematically, multiplying
        //a variable by 0 will not affect other variables when we're adding them together
        return 0;
    }

    /**
     * Get the coefficient that is stored respective to an index in the LinkedList
     *
     * @param index The index of the coefficient to retrieve
     *
     * @return The coefficient at the specified index
     */
    public int getCoefficientByIndex(int index)
    {
        return getValueFromIndex(index, "coefficient");
    }

    /**
     * Get the exponent that is stored respective to an index in the LinkedList
     *
     * @param index The index of the exponent to retrieve
     *
     * @return The exponent at the specified index
     */
    public int getExponentByIndex(int index)
    {
        return getValueFromIndex(index, "exponent");
    }

    /**
     * Sort the linked list using a Selection sort method because we don't necessarily need efficient with small numbers
     */
    public void sort()
    {
        int i, j, lowestExponent, tempCoefficient, tempExponent;

        for (i = size() - 1; i > 0; i--) // Let's loop backwards
        {
            lowestExponent = 0; // Our lowest number will always be the first one at the beginning, so let's use index 0

            for (j = 1; j <= i; j ++) // Locate the smallest element between positions 1 and i.
            {
                // Compare if there's even a smaller number
                if (this.getExponentByIndex(j) < this.getExponentByIndex(lowestExponent))
                {
                    lowestExponent = j; // We found an even smaller number, let's save it
                }
            }

            // Save the values temporarily so we don't lose them
            tempCoefficient = this.getCoefficientByIndex(lowestExponent);
            tempExponent = this.getExponentByIndex(lowestExponent);

            // Add the new lowest exponent to location right before the respective one and remove the one next to it
            this.add(lowestExponent, this.getCoefficientByIndex(i), this.getExponentByIndex(i));
            this.remove(lowestExponent + 1);

            // Add the old exponent to the location right before the one we just swapped and remove the one we swapped
            this.add(i, tempCoefficient, tempExponent);
            this.remove(i + 1);
        }
    }

    /**
     * Get the coefficient or exponent that is stored in the LinkedList
     *
     * @param index The index of the value to retrieve
     * @param value Retrieve either the exponent or coefficient
     *
     * @return The respective coefficient or exponent
     */
    private int getValueFromIndex(int index, String value)
    {
        //forces the index to be valid
        assert (index >= 0 && index < size());

        temp = head; //start at the head of the list

        //iterate to the correct node
        for (int i = 0; i < index; i++)
        {
            temp = temp.next;
        }

        if (value.equals("coefficient"))
            return temp.coefficient; // And return the corresponding element
        else
            return temp.exponent; // And return the corresponding element
    }

    /**
     * Add an element to the LinkedList with a specific index
     *
     * @param index         The location of where it needs to be added
     * @param _coefficient  The coefficient that will be stored
     * @param _exponent     The exponent that will be stored
     */
    private void add(int index, int _coefficient, int _exponent)
    {
        if (index == size()) // We want to add to the end of the list, just call the other more efficient function
        {
            add(_coefficient, _exponent);
            return;
        }
        else if (index == 0) // Add the element to the beginning
        {
            Node<E> temp = new Node<E>();
            temp.coefficient = _coefficient;
            temp.exponent = _exponent;
            temp.next = head;
            head.previous = temp;
            head = temp;
            counter++;
            return;
        }

        // Here, we start to see the temp node come into play.
        // We use it to track the current node without modifying
        // the head node.

        temp = head;

        for (int i = 0; i < index-1; i++)
        {
            temp = temp.next; // Move to the next node
        }

        Node<E> myNode = new Node<E>();    // Create a new node
        myNode.coefficient = _coefficient; // And set the coefficient
        myNode.exponent = _exponent;       // And set the exponent
        myNode.next = temp.next;           // Set it to point to the next coefficient
        temp.next = myNode;                // Set the previous coefficient to point to it
        counter++;                         // Increment the count;
    }

    /**
     * Remove an element from the LinkedList
     *
     * @param index The index of the element to remove
     */
    private void remove(int index)
    {
        assert(index >= 0 && index < size()); // Force a valid index
        temp = head; // Start at the beginning of the list

        if (index == 0) // We want to remove the first item
        {
            head = head.next; // Have the head point to the next one
            counter--; // Update the count
        }
        else if(index == size()) // We want to remove the last one
        {
            tail = tail.previous; // Have the tail point to the second to the last
            counter--; // Update the count
        }

        // Iterate to the position before the index
        for (int i = 0; i < index-1; i++)
        {
            temp = temp.next;
        }

        Node<E> two = temp.next;

        // Set temp.next to point to the Node next to the Node to be removed
        temp.next = two.next;
        counter--; //decrement size
    }
}