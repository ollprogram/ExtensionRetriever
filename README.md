# ExtensionRetriever
Retrieve the right extension for a file. Using an xml database containing signatures.
# Author and Licence :
Hi I'm ollprogram and I am the author of this project. Thanks for using it. </br>Please don't be afraid to report bugs or mistakes to me. I'll try to fix them. By the way, english isn't my main language, so I'm sorry for english mistakes but I'm open if you find some. </br>
Find information about the licence used for this project [here](https://github.com/ollprogram/ExtensionRetriever/blob/main/LICENSE).
You need to know the licence before using my project. It gives information about how you can use it.
# Description :
With this application you will be able to retrieve the right extension for a file which don't have any extension or not the proper one.
</br>This application will retrieve the proper extension by using an xml file which contains some signature.
</br>This [xml database](https://github.com/ollprogram/ExtensionRetriever/blob/main/resources/filesTypes.xml) could be extended as you wish by using this [dtd](https://github.com/ollprogram/ExtensionRetriever/blob/main/resources/filesTypes.dtd) .
# Dependencies :
For this project I'm using java 18. So you couldn't lauch it with older versions.
# Download :
You can download the runnable jar file and the xml database [here](https://github.com/ollprogram/ExtensionRetriever/releases).
# Usage :
<h3> To launch the program with the console interface : </h3>
First add the filesTypes.xml in the same directory as ExtensionRetriever.jar. Then,
<code>java -jar ExtensionRetriever.jar</code> (cmd or terminal)

<h3> Different options will appear on your screen : </h3>
<ul>
  <li>Add file to selection. You should paste an absolute path to the file chosen.</li>
  <li>Add all files from a directory to a selection. You should paste an absolute path to the directory chosen.</li>
  <li>Analyse extensions. This will analyse the files selected to retrieve their proper extension.</li>
  <li>Change all extensions (rename). This will rename all selected files to apply the analysed extension for each file.</li>
  <li>Clear selection</li>
  <li>Exit</li>
</ul>

# Next Upates :
- Better exceptions caught.
