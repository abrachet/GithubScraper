# this does nothing, it doesn't work
# wikipedia pages not standardized in how they
# label language file extensions
# ended up doing this by hand >:|

import requests
from bs4 import BeautifulSoup

k = requests.get("https://en.wikipedia.org/wiki/List_of_programming_languages")
soup = BeautifulSoup(k.text, 'html.parser')

lis = soup.find_all('li')

file = open("languages.csv", "w+")

def find_extension(f, url):
    wiki = requests.get(str(url))
    soup = BeautifulSoup(wiki.text, 'html.parser')

    arr = []

    try:
        lang_name = soup.find('h1', id="firstHeading").text
    except:
        return

    arr.append(lang_name.split(" (programming language)")[0] + ', ')
    try:
        for ext in soup.find('a', title="Filename extension").next.next.find_all('code'):
            arr.append(ext + ', ')
    except:
        pass
    try:
        for ext in soup.find('a', title="Filename extension").next.next.find_all('span', class_="monospaced"):
            arr.append(ext + ', ')
    except:
        pass

    if arr.len > 1:
        for elem in arr:
            f.write(elem)
        f.write('\n')


for tag in lis:
    for url in tag.find_all('a', href=True):
        print('https://en.wikipedia.org' + url['href'])
        find_extension(file, 'https://en.wikipedia.org' + url['href'])
