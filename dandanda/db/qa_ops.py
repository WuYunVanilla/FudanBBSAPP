from db import Session, Question, Answer

class QAOp():
    @staticmethod
    def insertQuestion(questioner, title, label, description, isPublished):
        sess = Session()
        new_question = Question(questioner=questioner, title=title, label=label, description=description,
                                isPublished=isPublished, numFollowers=0, numAnswers=0)
        sess.add(new_question)
        sess.commit()


    @staticmethod
    def add_num_answers(questionId):
        sess = Session()
        que = sess.query(Question).filter_by(id=questionId).first()
        old_num = que.numAnswers
        que.numAnswers = old_num+1
        sess.commit()

    @staticmethod
    def printQuestions():
        sess = Session()
        for instance in sess.query(Question).order_by(Question.id):
            print instance
        sess.commit()

    @staticmethod
    def insertAnswer(answerer, questionID, content):
        sess = Session()
        new_answer = Answer(answerer=answerer, questionID=questionID, content=content,
                            numAgree=0, numCollect=0)
        sess.add(new_answer)
        sess.commit()

    @staticmethod
    def printAnswers():
        sess = Session()
        for instance in sess.query(Answer).order_by(Answer.id):
            print instance
        sess.commit()


    @staticmethod
    def showQuestions(num):
        list1 = []
        list2 = []
        n = 0
        sess = Session()
        for instance in sess.query(Question):
            if instance.isPublished:
                if n < num:
                    q_id = instance.id
                    ans = sess.query(Answer).filter_by(questionID=q_id).first()
                    list1.append(instance)
                    if ans:
                        list2.append(ans.content)
                    else:
                        list2.append("do not have any answer yet")
                    n += 1
                else:
                    break
            else:
                continue
        sess.commit()
        return list1, list2


    @staticmethod
    def showAnswers(q_id):
        list = []
        sess = Session()
        for instance in sess.query(Answer).filter_by(questionID=q_id):
            list.append(instance)
        sess.commit()
        return list

