
package sudoku.languages;
public abstract class Language {
    protected String languageString;
    //MAIN_WINDOW_DECLARATION
    protected String exitString;
    protected String newGameString;
    protected String cancelLastMove;
    protected String menuGame;
    protected String menuOptions;
    protected String itemControls;
    protected String itemAbout;
    protected String itemBest;  
    protected String itemRules; 
    protected String itemAnswer;
    protected String easy;
    protected String medium;
    protected String hard;
    protected String hardAndNoTips;
    protected String customTitle;
    protected String customMessage;
    protected String yes;
    protected String no;
    protected String cancel;
    protected String check;
    protected String beginNewGameQuestion;
    protected String beginNewGameTitle;
    protected String begin;
    protected String chooseDifficulty;
    protected String tableFilledMessage;
    protected String tableFilledTitle;
    protected String wrongResultMessage;
    protected String wrongResultTitle;
    protected String allISCorrect;
    protected String lookAtYourselfTitle;
    protected String playAgain;
    protected String saveGameQuestion;
    protected String saveGameYes;
    protected String saveGameNo;
    protected String exitTitle;
    protected String exitMessage;
    protected String savedGameWasLoaded;
    protected String labelLanguage;
    //ABOUT_WINDOW_DECLARATION
     protected String aboutString, aboutTitle;
 
     
    public final String getLanguageString() { return languageString;   }
    //MAIN_WINDOW_DECLARATION

    public String getExitString()           {   return exitString;             }
    public String getNewGameString()        {   return newGameString;          }
    public String getCancelLastMove()       {   return cancelLastMove;         }
    public String getMenuGame()             {   return menuGame;               }
    public String getMenuOptions()          {   return menuOptions;            }
    public String getItemControls()         {   return itemControls;           }
    public String getItemAbout()            {   return itemAbout;              }
    public String getItemBest()             {   return itemBest;               }
    public String getItemRules()            {   return itemRules;              }
    public String getItemAnswer()           {   return itemAnswer;             }
    public String getEasy()                 {   return easy;                   }
    public String getMedium()               {   return medium;                 }
    public String getHard()                 {   return hard;                   }
    public String getHardAndNoTips()        {   return hardAndNoTips;          }
    public String getCustomTitle()          {   return customTitle;            }
    public String getCustomMessage()        {   return customMessage;          }
    public String getYes()                  {   return yes;                    }
    public String getNo()                   {   return no;                     }
    public String getCancel()               {   return cancel;                 }
    public String getCheck()                {   return check;                  }
    public String getBeginNewGameQuestion() {   return beginNewGameQuestion;   }
    public String getBeginNewGameTitle()    {   return beginNewGameTitle;      }
    public String getBegin()                {   return begin;                  }
    public String getChooseDifficulty()     {   return chooseDifficulty;       }
    public String getTableFilledMessage()   {   return tableFilledMessage;     }
    public String getTableFilledTitle()     {   return tableFilledTitle;       }
    public String getWrongResultMessage()   {   return wrongResultMessage;     }
    public String getWrongResultTitle()     {   return wrongResultTitle;       }
    public String getAllISCorrect()         {   return allISCorrect;           }
    public String getLookAtYourselfTitle()  {   return lookAtYourselfTitle;    }
    public String getPlayAgain()            {   return playAgain;              }
    public String getSaveGameQuestion()     {   return saveGameQuestion;       }
    public String getSaveGameYes()          {   return saveGameYes;            }
    public String getSaveGameNo()           {   return saveGameNo;             }
    public String getExitTitle()            {   return exitTitle;              }
    public String getExitMessage()          {   return exitMessage;            }
    public String getSavedGameWasLoaded()   {   return savedGameWasLoaded;     }
    public String getLabelLanguage()        {   return labelLanguage;          }

    
    
    
    //ABOUT_WINDOW_GETTERS
    public final String getAboutString()    { return aboutString;      }
    public final String getAboutTitle()     { return aboutTitle;       }
    
     
     
}
