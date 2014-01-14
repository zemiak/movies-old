package com.zemiak.batch.metadata.description;

import javax.enterprise.context.Dependent;

/**
 *
 * @author vasko
 */
@Dependent
public class Csfd implements IDescriptionReader {
    private static final String URL1 = "www.csfd.cz/";
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
    if ((is_null($row->description) || trim($row->description) == '') && (strpos($row->url, 'www.csfd.cz') !== false)) {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $row->url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $output = curl_exec($ch);
        curl_close($ch);

        $description = '';
        $lines = explode("\n", $output);
        $i = 0;

        for ($i = 0; $i < count($lines); $i++) {
            $line = $lines[$i];

            if (strpos($line, $beforeDesc) !== false) {
                $description = trim($lines[$i + 1]);
                if ($description == "") $description = trim($lines[$i + 2]);
                $pos = strpos($description, '&nbsp;');
                if ($pos !== false) $description = trim(substr($description, 0, $pos));
                break;
            }
        }

        $description = str_replace('<BR>', "\n", $description);
        $descriptionAscii = iconv("UTF-8", "ASCII//TRANSLIT", $description);
        
        $row->description = $description;
        $id = $row->id;
        unset($row->id);

        $movie->update($id, $row);
        echo "$id ";
    }
    */
}
