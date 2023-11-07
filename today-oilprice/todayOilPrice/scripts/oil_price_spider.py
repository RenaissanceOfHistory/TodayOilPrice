import requests
from bs4 import BeautifulSoup


def spider_info(url: str) -> list:
    """
    爬取指定url页面信息
    :param url:
    :return:
    """
    # 请求连接
    with requests.get(url) as resp:
        # 创建soup对象，便于数据操作
        soup = BeautifulSoup(resp.content, "html.parser")
        # 定位页面元素
        citems = soup.find("span", class_="citems")
        li_list: list = citems.find_all("li")
        data_list = []
        col_list = ["area", "label_89", "label_92", "label_95", "label_0"]

        # 遍历元素列表
        for li in li_list:
            # 获取a链接列表
            a_list: list = li.find_all("a")
            # 将链接列表的文本添加到临时集合中
            tmp_list = [item.string for item in a_list]
            # 数据列表添加字典数据
            data_list.append(dict(zip(col_list, tmp_list)))
        return data_list


def get_province() -> list:
    """
    获取所有省份信息
    :return:
    """
    # 目标链接
    url = r"http://youjia.tianqi5.cn/"
    # 请求数据
    with requests.get(url) as resp:
        soup = BeautifulSoup(resp.content, "html.parser")
        # 定位元素
        clists = soup.find("span", class_="clists")
        a_list: list = clists.find_all("a")
        data_list = []

        # 遍历省份列表，添加省份信息
        for item in a_list:
            data = {
                "href": item["href"],
                "title": item["title"],
                "text": item.string
            }
            data_list.append(data)
    return data_list


def get_specific_oil_info(label: str) -> list:
    """
    获取指定标号（0#、92#、95#）页面数据
    :param label:
    :return:
    """
    # 请求链接（0表示柴油，页面地址不同于其它页面）
    url = rf"http://youjia.tianqi5.cn/{'chaiyou.html' if ('0' == label) else label + 'haoqiyou.html'}"
    with requests.get(url) as resp:
        soup = BeautifulSoup(resp.content, "html.parser")
        citems = soup.find("span", class_="citems")
        li_list = citems.find_all("li")
        col_list = ["area", "price"]
        data_list = []

        # 指定标号数据添加进数据列表
        for li in li_list:
            span_list: list = li.find_all("span")
            tmp_list = [item.string for item in span_list]
            data_list.append(dict(zip(col_list, tmp_list)))
        return data_list


def spider():
    # 遍历省份，爬取省份油价信息
    for province in get_province():
        print(f"\n\n{province['text']}")
        spider_info(province["href"])


def main() -> None:
    spider()


if __name__ == '__main__':
    main()
