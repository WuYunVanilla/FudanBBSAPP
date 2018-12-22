from sqlalchemy import *
from sqlalchemy.orm import sessionmaker

from tables import *
import config as cf

engine = create_engine(cf.db_path)
Base.metadata.create_all(engine, checkfirst=True)
Session = sessionmaker(bind=engine)
