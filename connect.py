import requests as r
from bs4 import BeautifulSoup as bs
import praw
import pprint

reddit = praw.Reddit(client_id='EAV80_8lk6SnBA',
                     client_secret='1lZTdtEne47njSxI990tTwZ1Ac8',
                     user_agent='<Mac>:<app ID>:<version one> (by /u/<New_Canary>)')

# links = {}
pp = pprint.PrettyPrinter(indent=4)

for submission in reddit.subreddit('upliftingnews').top(limit=100):
    #links[submission.title] = submission.url
    print(submission.title + " " + submission.url)

#print (links)