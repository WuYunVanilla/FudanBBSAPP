from db import Session, Question, Answer
from sqlalchemy import desc, func
import time


class QAOp():
    @staticmethod
    def insertQuestion(questioner, title, label, description, isPublished):
        sess = Session()
        date = time.strftime("%Y_%m_%d", time.localtime())
        new_question = Question(questioner=questioner, title=title, label=label, description=description,
                                isPublished=isPublished, numFollowers=0, numAnswers=0, date=date)
        sess.add(new_question)
        sess.commit()
        return new_question.id

    @staticmethod
    def getQ(q_id):
        sess = Session()
        que = sess.query(Question).filter_by(id=q_id).first()
        sess.commit()
        return que

    @staticmethod
    def add_num_answers(questionId):
        sess = Session()
        que = sess.query(Question).filter_by(id=questionId).first()
        old_num = que.numAnswers
        que.numAnswers = old_num+1
        sess.commit()

    @staticmethod
    def add_num_followers(questionId):
        sess = Session()
        que = sess.query(Question).filter_by(id=questionId).first()
        old_num = que.numFollowers
        que.numFollowers = old_num + 1
        sess.commit()

    @staticmethod
    def show_question_detail(q_id):
        sess = Session()
        que = sess.query(Question).filter_by(id=q_id).first()
        sess.commit()
        return que

    @staticmethod
    def printQuestions():
        sess = Session()
        for instance in sess.query(Question).order_by(Question.id):
            print instance
        sess.commit()

    @staticmethod
    def showQuestions():
        list1 = []
        list2 = []
        sess = Session()
        for instance in sess.query(Question).order_by(desc('numFollowers')):
            if instance.isPublished:
                q_id = instance.id
                ans = sess.query(Answer).filter_by(questionID=q_id).first()
                list1.append(instance)
                if ans:
                    list2.append(ans)
                else:
                    list2.append(None)
            else:
                continue
        sess.commit()
        return list1, list2

    @staticmethod
    def get_que_count():
        sess = Session()
        count = sess.query(func.count('*')).select_from(Question).scalar()
        sess.commit()
        return count

    @staticmethod
    def get_recommend(q_list):
        list1 = []
        list2 = []
        sess = Session()
        for q in q_list:
            que = sess.query(Question).filter_by(id=q).first()
            list1.append(que)
            ans = sess.query(Answer).filter_by(questionID=q).first()
            if ans:
                list2.append(ans)
            else:
                list2.append(None)
        sess.commit()
        return list1, list2

    @staticmethod
    def get_search(key):
        list1 = []
        list2 = []
        sess = Session()
        for instance in sess.query(Question):
            if instance.isPublished:
                title = instance.title
                label = instance.label
                descrip = instance.description
                all = title + label + descrip
                if key.lower() in all.lower():
                    list1.append(instance)
                    q_id = instance.id
                    ans = sess.query(Answer).filter_by(questionID=q_id).first()
                    if ans:
                        list2.append(ans)
                    else:
                        list2.append(None)
            else:
                continue
        sess.commit()
        return list1, list2


    ###############################################################################
    ###############################################################################
    ###############################################################################


    @staticmethod
    def insertAnswer(answerer, questionID, content, isPublished):
        sess = Session()
        date = time.strftime("%Y_%m_%d", time.localtime())
        new_answer = Answer(answerer=answerer, questionID=questionID, content=content, isPublished=isPublished,
                            numAgree=0, numCollect=0, date=date)
        sess.add(new_answer)
        sess.commit()
        return new_answer.id

    @staticmethod
    def add_num_agree(answerId):
        sess = Session()
        ans = sess.query(Answer).filter_by(id=answerId).first()
        old_num = ans.numAgree
        ans.numAgree = old_num + 1
        sess.commit()

    @staticmethod
    def add_num_collect(answerId):
        sess = Session()
        ans = sess.query(Answer).filter_by(id=answerId).first()
        old_num = ans.numCollect
        ans.numCollect = old_num + 1
        sess.commit()

    @staticmethod
    def show_answer_detail(a_id):
        sess = Session()
        ans = sess.query(Answer).filter_by(id=a_id).first()
        sess.commit()
        return ans

    @staticmethod
    def printAnswers():
        sess = Session()
        for instance in sess.query(Answer).order_by(Answer.id):
            print instance
        sess.commit()

    @staticmethod
    def showAnswers(q_id):
        list = []
        sess = Session()
        for instance in sess.query(Answer).filter_by(questionID=q_id):
            if instance.isPublished:
                ans = [instance.id, instance.answerer, instance.content,
                       instance.numAgree, instance.numCollect, instance.date]
                list.append(ans)
        sess.commit()
        return list
