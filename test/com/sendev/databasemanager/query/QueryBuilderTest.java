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
        assertEquals(create().take(5).skip(10).toSQL(), "SELECT * FROM `test_users` LIMIT 5 OFFSET 10;");
    }

    @Test
    public void testJoinTableParamGetsFormattedWithDatabasePrefix()
    {
        assertEquals(create().innerJoin("groups", "users.id", "groups.id").toSQL(), "SELECT * FROM `test_users` INNER JOIN `test_groups` ON `test_users`.`id` = `test_groups`.`id`;");
    }

    @Test
    public void testJoinTableParamaIgnoresDatabasePrefixIfToldTo()
    {
        assertEquals(create("users", "true").innerJoin("groups", "users.id", "groups.id").toSQL(), "SELECT * FROM `users` INNER JOIN `groups` ON `users`.`id` = `groups`.`id`;");
    }

    @Test
    public void testJoiningMultipleTables()
    {
        QueryBuilder builder = create();

        builder.innerJoin("groups").on("users.id", "groups.user_id");
        builder.innerJoin("taks").on("users.id", "taks.user_id");

        assertEquals(builder.toSQL(), "SELECT * FROM `test_users` INNER JOIN `test_groups` ON `test_users`.`id` = `test_groups`.`user_id` INNER JOIN `test_taks` ON `test_users`.`id` = `test_taks`.`user_id`;");
    }

    @Test
    public void testJoiningOut()
    {
        assertEquals(create().outerJoin("groups", "users.id", "groups.user_id").toSQL(), "SELECT * FROM `test_users` OUTER JOIN `test_groups` ON `test_users`.`id` = `test_groups`.`user_id`;");
    }

    @Test
    public void testJoiningFull()
    {
        assertEquals(create().fullJoin("groups", "users.id", "groups.user_id").toSQL(), "SELECT * FROM `test_users` FULL JOIN `test_groups` ON `test_users`.`id` = `test_groups`.`user_id`;");
    }

    @Test
    public void testJoiningLeft()
    {
        assertEquals(create().leftJoin("groups", "users.id", "groups.user_id").toSQL(), "SELECT * FROM `test_users` LEFT JOIN `test_groups` ON `test_users`.`id` = `test_groups`.`user_id`;");
    }

    @Test
    public void testJoiningRight()
    {
        assertEquals(create().rightJoin("groups", "users.id", "groups.user_id").toSQL(), "SELECT * FROM `test_users` RIGHT JOIN `test_groups` ON `test_users`.`id` = `test_groups`.`user_id`;");
    }

    @Test
    public void testJoiningCustom()
    {
        QueryBuilder builder = create();

        builder.join("groups", "potato").on("users.id", "groups.user_id");

        assertEquals(builder.toSQL(), "SELECT * FROM `test_users` POTATO JOIN `test_groups` ON `test_users`.`id` = `test_groups`.`user_id`;");
    }

    private QueryBuilder create(String... items)
    {
        if (items.length == 0) {
            return new QueryBuilder("users");
        }

        if (items.length == 1) {
            return new QueryBuilder(items[0]);
        }

        if (items[1].equalsIgnoreCase("true") || items[1].equalsIgnoreCase("1")) {
            return new QueryBuilder(items[0], true);
        }

        return new QueryBuilder(items[0]);
    }
}
