package com.bvisiongames.me.license;

/**
 * Created by Sultan on 7/26/2015.
 */
public class Credit {

    public eachCredit[] list = new eachCredit[]{
            new eachCredit("Sam Sultan","lebro.xyz")
    };

    public class eachCredit{

        private String author, url;

        public eachCredit(String author, String url){

            this.author = author;
            this.url = url;

        }

        public String getAuthor(){
            return author;
        }
        public String getUrl(){
            return url;
        }

    }
}
