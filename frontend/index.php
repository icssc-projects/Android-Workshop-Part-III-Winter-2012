<?php
/**
* GET Search
*
* An example of how a parameter being passed in from the URL via GET or POST
* can be used to query a database and return results.
* 
* Android Workshop W12 - Part Three
*
* @author		Adam Brenner <aebrenne@uci.edu>
* @link			http://appjam.roboteater.com/
**/

$parser = new searchParser($_GET['depname']);

class searchParser {

	private $depName;
	private $mysqlHost = "";
	private $mysqlUser = "";
	private $mysqlPass = "";
	private $mysqlDB   = "";

	function __construct($arg)
	{
		$this->depName = $arg;
		try {
			$this->_isArgEmpty();
			$this->_queryDatabase();
		} catch (Exception $e) {
			echo $e->getMessage();
		}
	}
	
	private function _isArgEmpty() {
		if(!isset($this->depName ) || empty($this->depName )) {
			throw new Exception("Arg variable is empty. Try ?depname=CS");
		}
	}
	
	private function _queryDatabase() {
		$mysql = $this->_connectToDatabase();
		$results = $this->_getResults($mysql);
		//echo json_encode(mysql_fetch_array($this->_getResults($mysql)));
		$rows = array();
		$count = 0;
		while($i = mysql_fetch_assoc($results)) {
		    $rows[$count]["classno"] = $i["name"];
			$rows[$count]["prof"] = $i["professor"];
			$count++;
		}
		echo json_encode($rows);
	}
	
	private function _connectToDatabase() {
		$mysql = mysql_connect($this->mysqlHost, $this->mysqlUser, $this->mysqlPass);
		if (!$mysql) {
		    die("Could not connect to MySQL: " . mysql_error());
		}
		mysql_select_db($this->mysqlDB);
		
		return $mysql;
	}
	
	private function _getResults($mysql) {
		$this->depName = mysql_real_escape_string($this->depName);
		$result = mysql_query("SELECT name, department, professor FROM classes WHERE department = '".$this->depName."'");
		if (!$result) {
		    die("Invalid query: " . mysql_error());
		}
		return $result;
	}
}
?>