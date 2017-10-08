/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sera
 */
public class VertexVisitor implements Visitor<String> {

    @Override
    public void visit(String obj) {
        System.out.println(obj);
    }
    
}