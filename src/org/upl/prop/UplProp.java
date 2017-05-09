package org.upl.prop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UplProp
{
  Properties prop;
  
  public Properties getDBProperty()
  {
    this.prop = new Properties();
    ClassLoader classLoader = getClass().getClassLoader();
    try
    {
      InputStream url = classLoader.getResourceAsStream("org/upl/prop/uplProplocal.properties");
      this.prop.load(url);
    }
    catch (IOException asd)
    {
      asd.printStackTrace();
    }
    return this.prop;
  }
}
