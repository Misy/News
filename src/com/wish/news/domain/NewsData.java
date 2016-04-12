package com.wish.news.domain;

import java.util.ArrayList;

public class NewsData {
	public int retcode;
	public ArrayList<NewsMenuData> data;

	@Override
	public String toString() {
		return "NewsData [retcode=" + retcode + ", data=" + data + "]";
	}

	public class NewsMenuData {
		public String id;
		public String title;
		public int type;
		public String url;
		public ArrayList<NewsTabData> children;

		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", children=" + children
					+ "]";
		}

	}

	public class NewsTabData {
		public String id;
		public String title;
		public int type;
		public String url;

		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}

	}

}
