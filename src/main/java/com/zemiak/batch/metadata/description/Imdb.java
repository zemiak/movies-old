package com.zemiak.batch.metadata.description;

/**
 *
 * @author vasko
 */
public class Imdb implements IDescriptionReader {
    private static final String URL1 = "www.imdb.com/";
    private static final String URL2 = "http://" + URL1;

    @Override
    public boolean acceptsUrl(final String url) {
        return url.startsWith(URL1) || url.startsWith(URL2);
    }

    @Override
    public String getDescription(final String url) {
        return null;
    }
    
    /*
    if (! (is_null($row->description) || $row->description == '')) continue;

    if (strpos($row->url, 'www.imdb.com') !== false) {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $row->url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $output = curl_exec($ch);
        curl_close($ch);

        $description = '';
        $lines = explode("\n", $output);
        $i = 0;

        for ($i = 0; $i < count($lines); $i++) {
            $line = trim($lines[$i]);

            if (strpos($line, $descBegin) === 0) {
                $description = substr($line, strlen($descBegin));
                $pos = strpos($description, '"');
                if ($pos !== false) $description = trim(substr($description, 0, $pos));
                break;
            }
        }

        $row->description = $description;
        $id = $row->id;
        unset($row->id);

        $movie->update($id, $row);
        echo "$id ";
    }
    */
}
