for FILE in *.png
do
 FILENAME=`echo $FILE | cut -d "." -f1` 
 FILENAME=`echo $FILENAME | awk '{print toupper($0)}'`
 FILENAME="${FILENAME}.png"
 echo "$FILE => ${FILENAME}"
 mv $FILE $FILENAME
done

