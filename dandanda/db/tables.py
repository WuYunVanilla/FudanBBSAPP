from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Boolean

Base = declarative_base()


class User(Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True)
    stdId = Column(String)
    password = Column(String)
    valid = Column(Boolean)
    nickname = Column(String)
    gender = Column(String)
    grade = Column(String)
    signature = Column(String)
    photo = Column(String)
    posting = Column(String)
    answer = Column(String)
    follow = Column(String)
    collection = Column(String)
    view = Column(String)

    def __repr__(self):
        return '%s(%r, %r, %r, %r, %r, %r, %r, %r, %r, %r, %r, %r)' % (self.__class__.__name__, self.stdId,
                                                                       self.password, self.valid,
                                                                       self.nickname, self.gender,
                                                                       self.grade, self.signature,
                                                                       self.posting, self.answer,
                                                                       self.follow, self.collection,
                                                                       self.view)


class Question(Base):
    __tablename__ = 'questions'
    id = Column(Integer, primary_key=True)
    questioner = Column(String)
    title = Column(String)
    label = Column(String)
    description = Column(String)
    isPublished = Column(Boolean)
    numFollowers = Column(Integer)
    numAnswers = Column(Integer)
    date = Column(String)

    def __repr__(self):
        return '%s(%r, %r, %r, %r, %r, %r, %r, %r, %r)' % (self.__class__.__name__, self.id, self.title,
                                                       self.label, self.description, self.questioner,
                                                       self.isPublished, self.numFollowers, self.numAnswers,
                                                       self.date)


class Answer(Base):
    __tablename__ = 'answers'
    id = Column(Integer, primary_key=True)
    answerer = Column(String)
    questionID = Column(Integer)
    content = Column(String)
    isPublished = Column(Boolean)
    numAgree = Column(Integer)
    numCollect = Column(Integer)
    date = Column(String)

    def __repr__(self):
        return '%s(%r, %r, %r, %r, %r, %r, %r, %r)' % (self.__class__.__name__, self.id, self.answerer, self.questionID,
                                               self.content, self.isPublished, self.numAgree, self.numCollect,self.date)

