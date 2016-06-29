<?php

// make sure browsers see this page as utf-8 encoded HTML
header('Content-Type: text/html; charset=utf-8');

$limit = 10;
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;

$results = false;

if ($query)
{
		
  // The Apache Solr Client library should be on the include path
  // which is usually most easily accomplished by placing in the
  // same directory as this script ( . or current directory is a default
  // php include path entry in the php.ini)

  require_once('Apache/Solr/Service.php');

  // create a new solr service instance - host, port, and webapp
  // path (all defaults in this example)
	



  $solr = new Apache_Solr_Service('localhost',8983, '/solr/keckIndex/');
	
  // if magic quotes is enabled then stripslashes will be needed
  if (get_magic_quotes_gpc() == 1)
  {
	
    $query = stripslashes($query);
  }
  //
  // in production code you'll always want to use a try /catch for any
  // possible exceptions emitted  by searching (i.e. connection
  // problems or a query parsing error)
  
  try
  {
    if(isset($_GET['rankCheck'])){
	$addParams=array(
		'sort'=>'pageRankFile desc',
	);
	$results = $solr->search($query, 0, $limit,$addParams);
    }
    else{
	$results = $solr->search($query, 0, $limit);
    }    

  }
  catch (Exception $e)
  {
    // in production you'd probably log or email this error to an admin
    // and then show a special message to the user but for this example
    // we're going to show the full exception
    die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
  }
}

?>
<html>
  <head>
    <title>PHP Solr Client Example</title>
  </head>
  <body>
    <form  accept-charset="utf-8" method="get">
      <label for="q">Search:</label>
      <input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/><br/>
      <input type="checkbox" name="rankCheck"> Sort using Page Rank<br/>
      <input type="submit"/> 
    </form>
<?php

// display results
if ($results)
{
  $total = (int) $results->response->numFound;
  $start = min(1, $total);
  $end = min($limit, $total);
?>
    <div>Results <?php echo $start; ?> - <?php echo $end;?> of <?php echo $total; ?>:</div>
    <ol>
<?php
  // iterate result documents
  foreach ($results->response->docs as $doc)
  {
	$id=$doc->id;
	$title=$doc->title;
	$author=$doc->author;
	if($author=="")
	{
		$author="NA";
	}
	$url=str_replace("%22B22%","/",$id);
	$url=str_replace("%22col22%",":",$url);
	$url=str_replace("%22star22%","\\*",$url);
	$url=str_replace("%22Q22%","\\?",$url);
	$url=str_replace("%22Qoute22%","\\",$url);
	$url=str_replace("%22le22%","<",$url);
	$url=str_replace("%22gt22%",">",$url);
	$url=str_replace("/home/rab/shared/","",$url);
	$url=substr($url,0,-5);
	$url='http://'.$url;
	//echo $url;

?>
      <li>
        <p><a href="<?php echo $url;?>">Document</a> <?php echo $title?> Author: <?php echo $author?></p>
      </li>

<?php
    }
?>
        
      </li>

    </ol>
<?php
}
?>
  </body>
</html>
