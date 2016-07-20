package com.sendev.databasemanager.query;

import org.junit.Test;

import com.sendev.test.TestCase;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTest extends TestCase
{
    @Test
    public void testSelectAll()
    {
        assertEquals(create().toSQL(), "SELECT * FROM `test_users`;");
    }

    @Test
    public void testSelectAs()
    {
        assertEquals(create().select("id", "username AS name", "email").toSQL(), "SELECT `id`, `username` AS 'name', `email` FROM `test_users`;");
    }

    @Test
    public void testSelect()
    {
        assertEquals(create().select("id", "name", "age").toSQL(), "SELECT `id`, `name`, `age` FROM `test_users`;");
    }

    @Test
    public void testWhereSingle()
    {
        assertEquals(create().where("id", 5).toSQL(), "SELECT * FROM `test_users` WHERE `id` = 5;");
        assertEquals(create().where("name", "JohnDoe").toSQL(), "SELECT * FROM `test_users` WHERE `name` = 'JohnDoe';");
    }

    @Test
    public void testWhereAndOrCombination()
    {
        assertEquals(create().where("id", 5).orWhere("name", "JohnDoe").andWhere("age", 23).toSQL(), "SELECT * FROM `test_users` WHERE `id` = 5 OR `name` = 'JohnDoe' AND `age` = 23;");
        assertEquals(create().where("id", 5).andWhere("name", "JohnDoe").orWhere("age", 23).toSQL(), "SELECT * FROM `test_users` WHERE `id` = 5 AND `name` = 'JohnDoe' OR `age` = 23;");
    }

    @Test
    public void testWhereParameterGrouping()
    {
        assertEquals(create().where("id", 5).andWhere(( NestedClause builder ) -> {
            builder.where("name", "JohnDoe").orWhere("age", 23);
        }).toSQL(), "SELECT * FROM `test_users` WHERE `id` = 5 AND (`name` = 'JohnDoe' OR `age` = 23);");

        assertEquals(create().where("id", 5).orWhere(( NestedClause builder ) -> {
            builder.where("name", "JohnDoe").andWhere("age", 23);
        }).toSQL(), "SELECT * FROM `test_users` WHERE `id` = 5 OR (`name` = 'JohnDoe' AND `age` = 23);");
    }

    @Test
    public void testOrderBy()
    {
        assertEquals(create().orderBy("id").toSQL(), "SELECT * FROM `test_users` ORDER BY `id` ASC;");
        assertEquals(create().orderBy("id", "desc").toSQL(), "SELECT * FROM `test_users` ORDER BY `id` DESC;");
    }

    @Test
    public void testOrderInRandomOrder()
    {
        assertEquals(create().inRandomOrder().toSQL(), "SELECT * FROM `test_users` ORDER BY RAND();");
    }

    @Test
    public void testTake()
    {
        assertEquals(create().take(5).toSQL(), "SELECT * FROM `test_users` LIMIT 5;");
    }
    
    @Test
    public void testTakeAndOffset()
    {
        System.out.println(create().take(5).skip(10).toSQL());
        assertEquals(create().take(5).skip(10).toSQL(), "SELECT * FROM `test_users` LIMIT 5 OFFSET 10;");
    }

    private QueryBuilder create(String... items)
    {
        if (items.length == 0) {
            return new QueryBuilder("users");
        }

        return new QueryBuilder(items[0]);
    }
}
