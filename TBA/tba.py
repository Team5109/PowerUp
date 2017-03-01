import tbapi
import time
import ConfigParser
import textile
from subprocess import call

Config = ConfigParser.SafeConfigParser()
Config.read("config.ini")

team_key= Config.get('team','key')
event_id = Config.get('event','id')

usage_string="Displaying game info during competition in the pit"
version_number="v0.6"
parser = tbapi.TBAParser(5109, usage_string, version_number)

blue = '%{color:blue}Blue%' 
red = '%{color:red}Red%'
#print "Team {} finished in place {} with a record of {} at {}".format(team.team_number,rank,record,event.name)

def update(team_k,event_i):
    global team
    global event

    team = parser.get_team(team_k)
    event = parser.get_event(event_i)

    
#Method to convert the number of seconds in the game to hh:mm:ss
def sec_to_HMS(sec):

    #Convert the unix time to a whole number of seconds
    sec=int(sec)

    #Set the max number of seconds to 60
    seconds= sec%60

    #convert to minutes and set the max number of minutes to 60
    minutes = (sec/60)%60
    
    #convert to hours and set the max number of minutes to 60
    hours = (sec/3600)%60

    #Set the format
    return "{}:{}:{}".format(hours,minutes,seconds)

def main():
    thtml = "<notextile><head><script type=\"text/javascript\" src=\"scripts.js\"></script>\n<link rel=\"stylesheet\" type\"text/css\" href=\"main.css\"></head></notextile>\n\nTeam {}, {}, from {} est {}\nCompetition: {}".format(team.team_number, team.nickname, team.location, team.rookie_year,event.name)
    thtml+= "<notextile><img src=\"logo.png\" height=\"63\" width=\"174\"></notextile>"
   
    event_ranking = parser.get_event_rankings(event_id)

    team_ranking_info = (event_ranking.get_rank_by_team(team.team_number))
    rank = team_ranking_info.raw[0]
    record = team_ranking_info.raw[7]
    rp = team_ranking_info.raw[2]
    thtml+="\nRecord: {} Rank: {} Ranking Points: {}\n<hr>\n".format(record, rank, rp)
    matches = parser.get_team_event_matches(team.key, event_id)

    def reorganize(alist):
	qm=[]
	qf=[]
	sf=[]
	f=[]
	for i in matches:
		if i.comp_level == 'qm':
			qm.append(i)
		if i.comp_level == 'qf':
			qf.append(i)
		if i.comp_level == 'sf':
			sf.append(i)
		if i.comp_level == 'f':
			f.append(i)
	return qm+qf+sf+f


    f_qm=False
    f_qf=False
    f_sf=False
    f_f=False
    
    temp = reorganize(matches)
    for i in temp:
	    outcome = i.alliances
	    if time.time()<float(i.time):
		    thtml+= "The next match ({}) will occur in {} at {}\n".format(i.match_number, sec_to_HMS(i.time-time.time()), time.strftime("%H:%M %B %d %Y", time.localtime(i.time)))
	    
            aliance = '%{color:blue}Blue%'
            for k in range(3):
                   if outcome['red']['teams'][k] == team.key:
                             aliance='%{color:red}Red%'
            if i.comp_level == 'qm':
	 	    if not f_qm:
                    	thtml+="""<notextile><hr>
				<h2>Qualifing Matches</h2>
				</notextile>\n"""
                    	f_qm=True
                    if time.time()<float(i.time):
		    #if 99999999<i.time:
			if not next_match_done:
				
                        	thtml+= "Match number {} will happen at {} (in {}).\nTeam {} aliance: {}\n\n".format(i.match_number, time.strftime("%H:%M %B %d %Y", time.localtime(i.time)), sec_to_HMS(i.time-time.time()), team.team_number, aliance) 
                    else:                   
                        statement=""
                        if outcome['blue']['score']>outcome['red']['score']:
                                if aliance == blue:
                                        statement = "Team {} and the {} alliance won by a score of {} to {}\n".format(team.team_number,blue, outcome['blue']['score'], outcome['red']['score'])
                                else:
                                        statement = "Team {} and the {} alliance lost by a score of {} to {}\n".format(team.team_number,blue, outcome['blue']['score'], outcome['red']['score'])
                        elif outcome['blue']['score']<outcome['red']['score']:
                                if aliance == red:
                                        statement = "Team {} and the {} alliance won by a score of {} to {}\n".format(team.team_number,red, outcome['blue']['score'], outcome['red']['score'])
                                else:
                                        statement = "Team {} and the {} alliance lost by a score of {} to {}\n".format(team.team_number,red, outcome['blue']['score'], outcome['red']['score'])

                        else:
                                statement="Both aliances tied with a score of {} to {}\n".format(team.team_number, outcome['blue']['score'], outcome['red']['score'])

                        thtml+= "Match {} was played at {}\n".format(i.match_number,time.strftime("%H:%M %B %d %Y", time.localtime(i.time)))
                        thtml+= statement+"\n\n"	
            
            elif i.comp_level == 'qf':
                    if not f_qf:
			f_qf=True
			thtml+="""<notextile><hr>
				<h2>Quarter Final Matches</h2>
				</notextile>\n"""
			
		    if time.time()<float(i.time):
				
                        thtml+= "Match number {} will happen at {} (in {}).\nTeam {} aliance: {}\n\n".format(i.match_number, time.strftime("%H:%M %B %d %Y", time.localtime(i.time)), sec_to_HMS(i.time-time.time()), team.team_number, aliance) 
                    else:                   
                        statement=""
                        if outcome['blue']['score']>outcome['red']['score']:
                                if aliance == blue:
                                        thtml+= "Team {} and the {} alliance won by a score of {} to {}\n".format(team.team_number,blue, outcome['blue']['score'], outcome['red']['score'])
                                else:
                                        thtml+= "Team {} and the {} alliance lost by a score of {} to {}\n".format(team.team_number,blue, outcome['blue']['score'], outcome['red']['score'])
                        elif outcome['blue']['score']<outcome['red']['score']:
                                if aliance == red:
                                        thtml+= "Team {} and the {} alliance won by a score of {} to {}\n".format(team.team_number,red, outcome['blue']['score'], outcome['red']['score'])
                                else:
                                        thtml+= "Team {} and the {} alliance lost by a score of {} to {}\n".format(team.team_number,red, outcome['blue']['score'], outcome['red']['score'])

                        else:
                                thtml+="Both aliances tied with a score of {} to {}\n".format(team.team_number, outcome['blue']['score'], outcome['red']['score'])
			
            elif i.comp_level == 'sf':
                    if not f_sf:
			f_sf=True
			thtml+="""<notextile><hr>
				<h2>Semi Final Matches</h2>
				</notextile>\n"""
		    if time.time()<float(i.time):
		    #if 99999999<i.time:
				
                        thtml+= "Match number {} will happen at {} (in {}).\nTeam {} aliance: {}\n\n".format(i.match_number, time.strftime("%H:%M %B %d %Y", time.localtime(i.time)), sec_to_HMS(i.time-time.time()), team.team_number, aliance) 
                    else:                   
                        
                        if outcome['blue']['score']>outcome['red']['score']:
                                if aliance == blue:
                                        thtml+= "Team {} and the {} alliance won by a score of {} to {}\n".format(team.team_number,blue, outcome['blue']['score'], outcome['red']['score'])
                                else:
                                        thtml+= "Team {} and the {} alliance lost by a score of {} to {}\n".format(team.team_number,blue, outcome['blue']['score'], outcome['red']['score'])
                        elif outcome['blue']['score']<outcome['red']['score']:
                                if aliance == red:
                                        thtml+= "Team {} and the {} alliance won by a score of {} to {}\n".format(team.team_number,red, outcome['blue']['score'], outcome['red']['score'])
                                else:
                                        thtml+= "Team {} and the {} alliance lost by a score of {} to {}\n".format(team.team_number,red, outcome['blue']['score'], outcome['red']['score'])

                        else:
                                thtml+="Both aliances tied with a score of {} to {}\n".format(team.team_number, outcome['blue']['score'], outcome['red']['score'])
			
            elif i.comp_level == 'f':
                    if not f_f:
			f_f=True
			thtml+="""<notextile><hr>
				<h2>Final Final Matches</h2>
				</notextile>\n"""
		    if time.time()<float(i.time):
			
				
                       	thtml+= "Match number {} will happen at {} (in {}).\nTeam {} aliance: {}\n\n".format(i.match_number, time.strftime("%H:%M %B %d %Y", time.localtime(i.time)), sec_to_HMS(i.time-time.time()), team.team_number, aliance) 
                    else:                   
                       	if outcome['blue']['score']>outcome['red']['score']:
                                if aliance == blue:
                                        thtml+= "Team {} and the {} alliance won by a score of {} to {}\n".format(team.team_number,blue, outcome['blue']['score'], outcome['red']['score'])
                                else:
                                        thtml+= "Team {} and the {} alliance lost by a score of {} to {}\n".format(team.team_number,blue, outcome['blue']['score'], outcome['red']['score'])
                        elif outcome['blue']['score']<outcome['red']['score']:
                                if aliance == red:
                                        thtml+= "Team {} and the {} alliance won by a score of {} to {}\n".format(team.team_number,red, outcome['blue']['score'], outcome['red']['score'])
                                else:
                                        thtml+= "Team {} and the {} alliance lost by a score of {} to {}\n".format(team.team_number,red, outcome['blue']['score'], outcome['red']['score'])

                        else:
                                thtml+="Both aliances tied with a score of {} to {}\n".format(team.team_number, outcome['blue']['score'], outcome['red']['score'])
			
    return (thtml+"<notextile><script type=\"text/javascript\">\nreload();\n</script><br />\n</notextile>")
				
update(team_key, event_id)
call(['rm','html/robot_info.html'])
call(['touch','html/robot_info.html'])
team_5109_is_Champions=True
while(team_5109_is_Champions==True):
   
    html = textile.textile(main())
    f = open("html/robot_info.html", 'r+')
    f.write(html)
    f.close()
    time.sleep(5) #Sleep for 5 seconds
    
