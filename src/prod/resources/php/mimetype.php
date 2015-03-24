<?php

/**
 * Returns a mime type of the file. Tries to use every possible extension.
 * Falls back to the classic extension-based searching, when no extension
 * is available.
 *
 * @param string $file
 * @return string
 */
function fileMimeType($file)
{
    if (function_exists('finfo_file')) {
        $handle = finfo_open(FILEINFO_MIME);
        $type = finfo_file($handle, $file);
        finfo_close($handle);
    } elseif (function_exists('mime_content_type')) {
        $type = mime_content_type($file);
    } else {
        $parts = explode('.', $file);
        $ext = strtolower(end($parts));

        switch ($ext) {
            case 'mp3':
            case 'wav':
            case 'au':
            case 'mpa':
            case 'aac':
            $type = 'audio/' . $ext;
            break;
            case 'qt':
            case 'mov':
            $type = 'video/quicktime';
            break;
            case 'mpv':
            case 'mp4':
            case 'm4v':
            $type = 'video/mp4';
            break;
            case 'avi':
            $type = 'video/' . $ext;
            break;
            case 'mpg':
            case 'mpeg':
            $type = 'video/mpeg4-generic';
            break;
            case 'rtf':
            case 'doc':
            case 'dot':
            case 'docx':
            $type = 'application/msword';
            break;
            case 'xls':
            case 'xlst':
            case 'xlsx':
            case 'csv':
            $type = 'application/vnd.ms-excel';
            break;
            case 'ppt':
            $type = 'application/vnd.ms-powerpoint';
            break;
            case 'pdf':
            $type = 'application/pdf';
            break;
            case 'png':
            case 'jpg':
            case 'jpeg':
            case 'jpe':
            case 'jpm':
            case 'jpx':
            case 'tiff':
            $type = 'image/' . $ext;
            break;
            default:
            $type = '';
            break;
        }
    }

    return $type;
}
