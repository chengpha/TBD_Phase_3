package com.example.warehouseapp.views;

import androidx.test.core.app.ActivityScenario;

import com.example.warehouseapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class MainActivityTest {
    private ActivityScenario<MainActivity> scenario;

    @Before
    public void setUp() throws Exception
    {
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    /**
     * Tests if the text that's meant to be on the left side is on the view.
     */
    @Test
    public void testUIIsDisplayed()
    {
        onView(withText(R.string.warehouse_id));
        onView(withText(R.string.warehouse_name));
        onView(withText(R.string.upload_file));
        onView(withText(R.string.select_warehouse));
        onView(withText(R.string.disable_enable_freight));
        onView(withText(R.string.add_shipment));
        onView(withText(R.string.delete));
        onView(withText(R.string.export_warehouse_shipments));
        onView(withText(R.string.display_all_shipments));
        onView(withId(R.id.btnChooseFile));
        onView(withId(R.id.spnSelectWarehouse));
        onView(withId(R.id.btnDisableEnableFreight));
        onView(withId(R.id.btnAddShipment));
        onView(withId(R.id.btnDeleteShipment));
        onView(withId(R.id.btnExportWarehouseShipments));
        onView(withId(R.id.btnDisplayAllShipments));
    }

    @Test
    public void testChooseFileButtonClicked()
    {
        assertNotNull(onView(withId(R.id.btnChooseFile)).perform(click()));
    }

    @Test
    public void testDisableEnableFreightReceiptClicked()
    {
        assertNotNull(onView(withId(R.id.btnDisableEnableFreight)).perform(click()));
    }

    @Test
    public void testAddShipmentButtonClicked()
    {
        assertNotNull(onView(withId(R.id.btnAddShipment)).perform(click()));
    }

    @Test
    public void testDeleteShipmentButtonClicked()
    {
        assertNotNull(onView(withId(R.id.btnDeleteShipment)).perform(click()));
    }

    @Test
    public void testLaunchDisplayAllShipmentsButtonClicked()
    {
        assertNotNull(onView(withId(R.id.btnDisplayAllShipments)).perform(click()));
    }

    @After
    public void tearDown() throws Exception
    {
        scenario.close();
    }
}