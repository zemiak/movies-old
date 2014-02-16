<?php

include_once('entity.php');
include_once('mimetype.php');
include_once('range.php');

/**
 * allowed dir prefix
 */
define('DIR_PREFIX', '/mnt/media/Movies/');

/**
 * plays the file
 */
function main()
{
    global $argv;
    $id = isset($_REQUEST['id']) ? $_REQUEST['id'] : (isset($argv[1]) ? $argv[1] : null);
    assert(!is_null($id));

    $file = readMovieFilename($id);
    $type = fileMimeType($file);

    handleFileRequest($type, $file);
}

function readMovieFilename($id) {
    $movie = new Movie();
    $entry = $movie->read($id);

    assert(!is_bool($entry));
    $file = DIR_PREFIX . $entry->file_name;
    assert(!is_null($file));
    
    return $file;
}

function handleFileRequest($type, $file) {
    if (empty($type)) {
        echo '<h1>File type ' . urlencode($file) . ' unknown</h1>';
    } else {
        if (! file_exists($file)) {
            echo '<h1>File does not exist: ' . urlencode($file) . '</h1>';
        } else {
            returnFile($type, $file);
        }
    }
}

function returnFile($type, $file) {
    header('Content-type: ' . $type);

    if (isRangeRequest())  {
        rangeDownload($file);
    } else {
        header("Content-Length: ".filesize($file));
        $fp = fopen($file, 'rb');
        fpassthru($fp);
    }
}

main();
