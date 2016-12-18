/*
 * Copyright (C) 2013 - Vladimir Jimenez
 * License: MIT
 */

package main;

import java.util.ArrayList;

public class Polynomial
{
    private LinkedList elements;

    /**
     * Create a Polynomial object in order to store information about a mathematical polynomial and allow for addition
     * and multiplication
     *
     * @param polynomial The polynomial as a regular string
     */
    public Polynomial(String polynomial)
    {
        boolean firstNumberIsNegative = false; // Keep track if the first number is negative
        elements = new LinkedList(); // Store our elements!

        if (polynomial.isEmpty())
            return;

        // Check if our first character is a negative, meaning our first coefficient will be negative
        if (polynomial.charAt(0) == '-')
        {
            firstNumberIsNegative = true; // Remember that the first coefficient was negative
            polynomial = polynomial.substring(1); // Get rid of it so it won't mess up my .split()
        }

        polynomial = polynomial.replaceAll("\\s+", ""); // Remove all white space
        String[] tokens = polynomial.split("\\+|-"); // Split the polynomial on all addition or subtraction

        for (String token : tokens) // Loop through all of our indeterminate
        {
            // Default values even though they get overwritten
            int coefficient, exponent;

            // Check to see that our token is in the valid format using a ReGex
            // ReGex <3
            if (token.matches("(\\d+\\w\\^\\(\\d+\\))|(\\w\\^\\(\\d+\\))|(\\d+\\w)|(\\w)|(\\d+)"))
            {
                // Get the coefficient (this will always be positive)
                coefficient = parseCoefficient(token);

                // Get the exponent, which will always been in between an open and close parenthesis
                try
                {
                    exponent = Integer.parseInt(token.substring(token.indexOf("(") + 1, token.lastIndexOf(")")));
                }
                catch (Exception e) // if there's no exponent then assume exponent 0 to make sense mathematically
                {
                    exponent = 0;
                }

                if (firstNumberIsNegative) // Check if the first number was negative
                {
                    coefficient = coefficient * -1; // Get the negative of it
                    firstNumberIsNegative = false; // Forget about it
                }
                // Check to see if we're not out of bounds and see if our coefficient was preceded by a minus
                else if (polynomial.indexOf(token) - 1 > 0 && polynomial.charAt(polynomial.indexOf(token) - 1) == '-')
                {
                    coefficient = coefficient * -1; // Get the negative
                }

                setCoefficient(coefficient, exponent); // And... Save!
            }
        }
    }

    /**
     * Add two polynomials together
     *
     * @param secondPoly The second polynomial to add to
     *
     * @return The sum of the polynomials
     */
    public Polynomial add(Polynomial secondPoly)
    {
        Polynomial newPoly = new Polynomial(""); // We'll store our new polynomial here
        boolean foundMatch;

        for (int i = 0; i < this.size(); i++) // Loop through all the groups of the first polynomial
        {
            foundMatch = false;

            for (int j = 0; j < secondPoly.size(); j++) // Loop through all the groups of the second polynomial
            {
                if (this.getExponentByIndex(i) == secondPoly.getExponentByIndex(j)) // Check if the exponents are equal
                {
                    // Add the coefficients
                    int newCoefficient = this.getCoefficientByIndex(i) + secondPoly.getCoefficientByIndex(j);

                    // Save the new coefficient and exponent in the new polynomial
                    newPoly.setCoefficient(newCoefficient, this.getExponentByIndex(i));

                    // We found a matching exponent, don't add it
                    foundMatch = true;

                    // Move to the next group
                    break;
                }
            }

            // If we didn't find a match, then save it to the polynomial
            if (!foundMatch)
            {
                newPoly.setCoefficient(this.getCoefficientByIndex(i), this.getExponentByIndex(i));
            }
        }

        // Loop through the second polynomial to see if we've skipped anything
        for (int k = 0; k < secondPoly.size(); k++)
        {
            // If the second polynomial has an exponent that the first polynomial doesn't have
            if (this.getCoefficient(secondPoly.getExponentByIndex(k)) == 0)
            {
                // Save the polynomial
                newPoly.setCoefficient(secondPoly.getCoefficientByIndex(k), secondPoly.getExponentByIndex(k));
            }
        }

        return newPoly;
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
        return elements.getCoefficientByIndex(index);
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
        return elements.getExponentByIndex(index);
    }

    /**
     * Get the coefficient corresponding to an exponent
     *
     * @param exponent The exponent we want to know to coefficient to
     *
     * @return The coefficient respective to the exponent or 0 if it's not found
     */
    public int getCoefficient(int exponent)
    {
        return elements.getCoefficient(exponent);
    }

    /**
     * Multiply two polynomials together
     *
     * @param secondPoly The second polynomial to be multiplied
     *
     * @return The product of the two polynomials
     */
    public Polynomial multiply(Polynomial secondPoly)
    {
        Polynomial newPoly = new Polynomial(""); // The product will be stored here

        for (int i = 0; i < this.size(); i++) // Loop through the first polynomial
        {
            for (int j = 0; j < secondPoly.size(); j++) // Loop through the second polynomial
            {
                // Do the math
                int newCoefficient = this.getCoefficientByIndex(i) * secondPoly.getCoefficientByIndex(j);
                int newExponent = this.getExponentByIndex(i) + secondPoly.getExponentByIndex(j);

                // Save it
                newPoly.setCoefficient(newCoefficient, newExponent);
            }
        }

        return newPoly;
    }

    /**
     * Add a pair of coefficient and exponent to the linked list
     *
     * @param coefficient The coefficient we'll be storing
     * @param exponent    The exponent we'll be storing
     */
    public void setCoefficient(int coefficient, int exponent)
    {
        elements.add(coefficient, exponent);
    }

    public Polynomial simplify()
    {
        Polynomial newPoly = new Polynomial(""); // Where the simplified polynomial will be stored
        int newCoefficient; // The total coefficient
        ArrayList<Integer> usedExponents = new ArrayList<Integer>(); // The exponents we've already added

        for (int i = 0; i < this.size(); i++) // Loop through the elements of the polynomial
        {
            if (usedExponents.contains(this.getExponentByIndex(i))) // Check if the exponent has been used already
            {
                continue;
            }

            newCoefficient = this.getCoefficientByIndex(i); // Get the value from the first element

            for (int j = 0; j < this.size(); j++) // Loop through all the elements
            {
                // Check if other elements have the same exponent
                if (i != j && this.getExponentByIndex(i) == this.getExponentByIndex(j))
                {
                    // Add the coefficient to the sum
                    newCoefficient += this.getCoefficientByIndex(j);
                }
            }

            usedExponents.add(this.getExponentByIndex(i)); // Save the exponent as used
            newPoly.setCoefficient(newCoefficient, this.getExponentByIndex(i)); // Save the new element to the polynomail
        }

        return newPoly;
    }

    /**
     * Get the total amount of values a polynomial has
     *
     * @return The number of values of the polynomial
     */
    public int size()
    {
        return elements.size();
    }

    /**
     * Generate the respective string equivalent of this object
     *
     * @return A string representation of this class
     */
    public String toString()
    {
        simplify(); // Add elements with common exponents
        elements.sort(); // Sort the LinkedList so it can look fabulous when we output it
        String string = ""; // The string we'll return

        for (int i = 0; i < elements.size(); i++) // Go through all the elements
        {
            if (elements.getCoefficientByIndex(i) == 0) // If the coefficient is 0, don't print it out
            {
                continue;
            }
            else if (elements.getExponentByIndex(i) == 0) // Exponent 0 means it essentially has no variable attached
            {
                string += String.format("%d", elements.getCoefficientByIndex(i));
            }
            else // If there is an exponent other than 0, then format it accordingly
            {
                if (elements.getCoefficientByIndex(i) == 1) // No need to put a coefficient of one
                {
                    string += String.format("x^(%d)", elements.getExponentByIndex(i));
                }
                else // Put every other coefficient
                {
                    string += String.format("%dx^(%d)", elements.getCoefficientByIndex(i), elements.getExponentByIndex(i));
                }
            }

            // Add a plus if we're not on the last element and if the next coefficient is positive
            if (i + 1 < elements.size() && elements.getCoefficientByIndex(i + 1) > 0)
            {
                string += "+";
            }
        }

        // Prettify the string by adding whitespace
        string = string.replace("+", " + ");
        string = string.replace("-", " - ").trim();

        // If out first element is a negative number, fix it
        if (string.charAt(0) == '-')
        {
            string = "-" + string.substring(2); // Add a minus and substring everything to get rid of the extra space
        }

        return string;
    }

    /**
     * Extract the integer value from a "token" of the original polynomial
     *
     * @param token The "token" we'll be examining
     *
     * @return The integer value of the coefficient or 1 if no coefficient was found
     */
    private int parseCoefficient(String token)
    {
        String temp = ""; // Just a default value

        for (int i = 0; i < token.length(); i++) // Loop through each char of the token
        {
            if (Character.isDigit(token.charAt(i))) // Check if it's a digit
            {
                temp += token.charAt(i); // Save it
            }
            else
            {
                if (i == 0) // If we hit this, means we have a coefficient of 1 (i.e. x^(10))
                    return 1;

                break; // We've saved the numbers already so we can leave
            }
        }

        return Integer.parseInt(temp); // Return the int value of the string we stored, this will always be positive
    }
}
