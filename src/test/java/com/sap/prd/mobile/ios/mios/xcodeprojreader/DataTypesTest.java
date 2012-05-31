package com.sap.prd.mobile.ios.mios.xcodeprojreader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.sap.prd.mobile.ios.mios.xcodeprojreader.jaxb.JAXBPlistParser;
import com.sap.prd.mobile.ios.mios.xcodeprojreader.jaxb.JAXBPlistParserTest;

public class DataTypesTest
{
  private Plist plist;
  private Dict dict;
  private Array array;

  @Before
  public void before() throws Exception
  {
    JAXBPlistParser parser = new JAXBPlistParser();
    plist = parser.load(JAXBPlistParserTest.fileName);
    dict = new ProjectFile(plist).getDict();
    array = dict.getArray("arraytest");
  }

  @Test
  public void stringInDict()
  {
    assertEquals("hello", dict.getString("str"));
    dict.setString("str", "world");
    assertEquals("world", dict.getString("str"));
  }

  @Test
  public void stringInArray()
  {
    assertEquals("value", array.getString(0));
    array.setString(0, "new");
    assertEquals("new", array.getString(0));
  }

  @Test
  public void integerInDict()
  {
    assertEquals(5, (int) dict.getInteger("int"));
    dict.setInteger("int", 6);
    assertEquals(6, (int) dict.getInteger("int"));
  }

  @Test
  public void integerInArray()
  {
    assertEquals(5, (int) array.getInteger(3));
    array.setInteger(3, 6);
    assertEquals(6, (int) array.getInteger(3));
  }

  @Test
  public void doubleInDict()
  {
    assertEquals(5.5, dict.getDouble("real"), 0.000001);
    dict.setDouble("real", 6.5);
    assertEquals(6.5, dict.getDouble("real"), 0.000001);
  }

  @Test
  public void doubleInArray()
  {
    assertEquals(5.5, array.getDouble(4), 0.000001);
    array.setDouble(4, 6.5);
    assertEquals(6.5, array.getDouble(4), 0.000001);
  }

  @Test
  public void boolInDict()
  {
    assertTrue(dict.getBool("bool_true"));
    dict.setBool("bool_true", false);
    assertEquals(false, dict.getBool("bool_true"));

    assertFalse(dict.getBool("bool_false"));
    dict.setBool("bool_false", true);
    assertEquals(true, dict.getBool("bool_false"));
  }

  @Test
  public void boolInArray()
  {
    assertTrue(array.getBool(5));
    array.setBool(5, false);
    assertEquals(false, array.getBool(5));

    assertFalse(array.getBool(6));
    array.setBool(6, true);
    assertEquals(true, array.getBool(6));
  }

  @Test
  public void dictInDict()
  {
    assertEquals("world", dict.getDict("dict").getString("hello"));
    Dict dict = plist.createDict();
    dict.setString("hello2", "world2");
    dict.setDict("dict", dict);
    assertEquals("world2", dict.getDict("dict").getString("hello2"));
  }

  @Test
  public void dictInDictNonExistingKey()
  {
    assertNull(dict.getDict("foo"));
  }

  @Test
  public void dictInArray()
  {
    assertEquals("value", array.getDict(1).getString("key"));
    Dict dict = plist.createDict();
    dict.setString("key2", "value2");
    array.setDict(1, dict);
    assertEquals("value2", array.getDict(1).getString("key2"));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void dictInArrayNonExistingIndex()
  {
    array.getDict(5000);
  }

  @Test
  public void getOrCreateAndSetArrayInDict()
  {
    Array array = dict.getOrCreateAndSetArray("arraytest");
    assertEquals(7, array.size());
    assertEquals("value", array.get(0));
  }
  
  @Test
  public void getOrCreateAndSetDictInDict()
  {
    Dict d = dict.getOrCreateAndSetDict("dict");
    assertEquals("world", d.getString("hello"));
  }

  @Test
  public void getOrCreateAndSetDictInDictNonExistingKey()
  {
    Dict d = dict.getOrCreateAndSetDict("foo");
    assertEquals(0, d.entrySet().size());
  }

  @Test
  public void getOrCreateAndSetArrayInDictNonExistingKey()
  {
    Array array = dict.getOrCreateAndSetArray("foo");
    assertEquals(0, array.size());
  }

  @Test
  public void arrayInDict()
  {
    assertEquals("value", dict.getArray("arraytest").getString(0));
    Array arr = plist.createArray();
    arr.addString("value2");
    dict.setArray("arraytest", arr);
    assertEquals("value2", dict.getArray("arraytest").getString(0));
  }

  @Test
  public void arrayInDictNonExistingKey()
  {
    assertNull(dict.getArray("foo"));
  }

  @Test
  public void arrayInArray()
  {
    assertEquals("test", array.getArray(2).getString(0));
    Array arr = plist.createArray();
    arr.addString("passed");
    array.setArray(2, arr);
    assertEquals("passed", array.getArray(2).getString(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void arrayInArrayNonExistingIndex()
  {
    array.getArray(5000);
  }
}