package com.sendev.databasemanager.seeder;

import java.text.DecimalFormat;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.sendev.databasemanager.seeder.contracts.FakeValuesServiceContract;
import com.sendev.databasemanager.seeder.contracts.Generator;
import com.sendev.databasemanager.seeder.contracts.ResolverContract;

public class Commerce extends Generator
{
    public Commerce(ResolverContract resolver, FakeValuesServiceContract fakeValueService)
    {
        super(resolver, fakeValueService);
    }

    public String color()
    {
        return service().fetchString("color.name");
    }

    public String department()
    {
        int numberOfDepartments = Math.max(random().nextInt(4), 1);
        SortedSet<String> departments = new TreeSet<String>();
        while (departments.size() < numberOfDepartments) {
            departments.add(service().fetchString("commerce.department"));
        }
        if (departments.size() > 1) {
            String lastDepartment = departments.last();
            return StringUtils.join(departments.headSet(lastDepartment), ", ") + " & " + lastDepartment;
        } else {
            return departments.first();
        }
    }

    public String productName()
    {
        return StringUtils.join(new String[]{service().fetchString("commerce.product_name.adjective"),
            service().fetchString("commerce.product_name.material"),
            service().fetchString("commerce.product_name.product")}, " ");
    }

    public String material()
    {
        return service().fetchString("commerce.product_name.material");
    }

    /**
     * Generate a random price between 0.00 and 100.00
     */
    public String price()
    {
        return price(0, 100);
    }

    public String price(double min, double max)
    {
        double price = min + (random().nextDouble() * (max - min));

        return new DecimalFormat("#0.00").format(price);
    }
}
