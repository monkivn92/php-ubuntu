<?php

class MyDB extends SQLite3 
{
	function __construct() 
	{
		$this->open('/media/DataBackup/PHP/swan');
	}
}

$db = new MyDB();
   if(!$db) {
      echo $db->lastErrorMsg();
   } else {
      echo "Opened database successfully\n";
   }

?>