package com.yangyun.config;

import com.baomidou.mybatisplus.core.parser.AbstractJsqlParser;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

/**
 * @author yangyun
 * @Description:
 * @date 2020/11/3 15:19
 */
public class SelfDefBlockAttackSqlParser extends AbstractJsqlParser {
    public SelfDefBlockAttackSqlParser() {

    }

    @Override
    public void processInsert(Insert insert) {

    }

    @Override
    public void processDelete(Delete delete) {
        Assert.notNull(delete.getWhere(), "Prohibition of full table deletion", new Object[0]);
        Expression e = delete.getWhere();
        String string = delete.toString();
        System.out.println(e);

        parse(e);

    }

    @Override
    public void processUpdate(Update update) {
        Assert.notNull(update.getWhere(), "Prohibition of table update operation", new Object[0]);
    }

    @Override
    public void processSelectBody(SelectBody selectBody) {

    }

    private void parse (Expression e){

        if (e instanceof OrExpression){
            OrExpression oe = (OrExpression)e;
            SimpleNode astNode = oe.getASTNode();
            if (astNode != null){

            }

            Expression leftExpression = oe.getLeftExpression();
            Expression rightExpression = oe.getRightExpression();
            parse(leftExpression);
            parse(rightExpression);
            System.out.println(111);
        }
        if (e instanceof AndExpression){
            AndExpression oe = (AndExpression)e;
            String stringExpression = oe.getStringExpression();
            Expression leftExpression = oe.getLeftExpression();
            Expression rightExpression = oe.getRightExpression();
            boolean not = oe.isNot();

            System.out.println(111);
        }

    }
}
